package com.ernest.reed

import android.net.Uri

enum class PostRatio { PORTRAIT_3_4, BALANCED_16_9 }

data class Post(
    val username: String,
    val timestamp: String,
    val text: String,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val imageUri: Uri? = null,
    val ratio: PostRatio = PostRatio.PORTRAIT_3_4
)

data class Moment(
    val label: String,
    val imageUri: Uri? = null
)

data class Reel(
    val username: String,
    val timestamp: String,
    val caption: String,
    val videoUri: android.net.Uri
)
