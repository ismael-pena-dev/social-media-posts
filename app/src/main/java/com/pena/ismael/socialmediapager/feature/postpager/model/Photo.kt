package com.pena.ismael.socialmediapager.feature.postpager.model

data class Photo(
    val photoId: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)