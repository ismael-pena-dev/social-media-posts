package com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity

data class Comment(
    val commentId: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String,
)