package com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto

data class CommentDto(
    val body: String,
    val email: String,
    val id: Int,
    val name: String,
    val postId: Int
)