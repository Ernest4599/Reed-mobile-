package com.ernest.reed

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

class CropVideoView(context: Context, attrs: AttributeSet? = null) : VideoView(context, attrs) {
    private var videoWidth = 0
    private var videoHeight = 0

    fun setVideoDimensions(width: Int, height: Int) {
        videoWidth = width
        videoHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val containerWidth = MeasureSpec.getSize(widthMeasureSpec)
        val containerHeight = MeasureSpec.getSize(heightMeasureSpec)

        if (videoWidth == 0 || videoHeight == 0 || containerWidth == 0 || containerHeight == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val videoRatio = videoWidth.toFloat() / videoHeight
        val containerRatio = containerWidth.toFloat() / containerHeight

        val finalWidth: Int
        val finalHeight: Int
        if (videoRatio > containerRatio) {
            finalHeight = containerHeight
            finalWidth = (containerHeight * videoRatio).toInt()
        } else {
            finalWidth = containerWidth
            finalHeight = (containerWidth / videoRatio).toInt()
        }
        setMeasuredDimension(finalWidth, finalHeight)
    }
}
