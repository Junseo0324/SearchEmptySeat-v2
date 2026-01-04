package com.example.searchplacement.presentation.user.review

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.review.ReviewResponse
import com.example.searchplacement.domain.model.ReviewRequest
import com.example.searchplacement.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _reviewSubmitSuccess = MutableStateFlow<Boolean?>(null)
    val reviewSubmitSuccess: StateFlow<Boolean?> get() = _reviewSubmitSuccess

    private val _reviewSubmitError = MutableStateFlow<String?>(null)
    val reviewSubmitError: StateFlow<String?> get() = _reviewSubmitError

    private val _reviews = MutableStateFlow<List<ReviewResponse>>(emptyList())
    val reviews: StateFlow<List<ReviewResponse>> get() = _reviews

    private val _reviewsError = MutableStateFlow<String?>(null)
    val reviewsError: StateFlow<String?> get() = _reviewsError

    fun submitReview(
        storePK: Long,
        rating: Float,
        content: String,
        imageUris: List<Uri>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = ReviewRequest(
                    storePK = storePK,
                    rating = rating,
                    content = content
                )

                val imageStrings = imageUris.map { it.toString() }
                
                val response = reviewRepository.registerReview(request, imageStrings)

                if (response.status == "success") {
                    onSuccess()
                } else {
                    onError(response?.message ?: "리뷰 등록 실패")
                }
            } catch (e: Exception) {
                onError("리뷰 등록 중 오류 발생: ${e.localizedMessage}")
            }
        }
    }


    fun getReviewsByStore(storePK: Long) {
        viewModelScope.launch {
            try {
                val response = reviewRepository.getReviewsByStore(storePK)
                if (response.status == "success") {
                    _reviews.value = response?.data ?: emptyList()
                    _reviewsError.value = null
                } else {
                    _reviewsError.value = response?.message ?: "리뷰 조회 실패"
                }
            } catch (e: Exception) {
                _reviewsError.value = "리뷰 조회 중 오류 발생: ${e.localizedMessage}"
            }
        }
    }

    fun clearReviewSubmitResult() {
        _reviewSubmitSuccess.value = null
        _reviewSubmitError.value = null
    }


}