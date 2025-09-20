package com.example.searchplacement.ui.user.review

import android.content.res.ColorStateList
import android.widget.RatingBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt

@Composable
fun RatingBarView(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    starColorHex: String = "#FFD700"
) {
    var internalRating by remember { mutableStateOf(rating) }

    AndroidView(
        factory = { context ->
            RatingBar(context).apply {
                numStars = 5
                stepSize = 0.5f
                this.rating = internalRating

                progressTintList = ColorStateList.valueOf(starColorHex.toColorInt())
                secondaryProgressTintList = ColorStateList.valueOf(starColorHex.toColorInt())
                backgroundTintList = ColorStateList.valueOf("#DDDDDD".toColorInt())


                setOnRatingBarChangeListener { _, newRating, _ ->
                    internalRating = newRating
                    onRatingChanged(newRating)
                }
            }
        },
        update = {
            if (it.rating != internalRating) {
                it.rating = internalRating
            }
        },
        modifier = modifier
    )
}
