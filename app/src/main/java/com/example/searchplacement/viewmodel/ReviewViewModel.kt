package com.example.searchplacement.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.review.ReviewResponse
import com.example.searchplacement.repository.ReviewRepository
import com.example.searchplacement.repository.UserRepository
import com.example.searchplacement.ui.utils.toMultipartPart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
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
        context: Context,
        storePK: Long,
        rating: Float,
        content: String,
        imageUris: List<Uri>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val requestJson = createReviewRequestBody(storePK, rating, content)

                var imageParts : MutableList<MultipartBody.Part> = emptyList<MultipartBody.Part>().toMutableList()
                imageUris.forEach { it ->
                    imageParts.add(toMultipartPart(context,it))
                }
                val response = reviewRepository.registerReview(requestJson, imageParts)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(response.body()?.message ?: "리뷰 등록 실패")
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
                if (response.isSuccessful) {
                    _reviews.value = response.body()?.data ?: emptyList()
                    _reviewsError.value = null
                } else {
                    _reviewsError.value = response.body()?.message ?: "리뷰 조회 실패"
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

    fun createReviewRequestBody(
        storePK: Long,
        rating: Float,
        content: String
    ): RequestBody {
        val json = JSONObject().apply {
            put("storePK", storePK)
            put("rating", rating)
            put("content", content)
        }
        return json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

}