package com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto

data class PhotoDto(
    val albumId: Int,
    val id: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)