package com.pena.ismael.socialmediapager.feature.postpager.model

data class Comment(
    val commentId: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String,
)