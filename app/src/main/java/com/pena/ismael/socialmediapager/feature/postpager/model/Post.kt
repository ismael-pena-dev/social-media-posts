package com.pena.ismael.socialmediapager.feature.postpager.model



sealed interface Post {
    val postId: Int
    val userId: Int
    val title: String
    data class TextPost(
        override val postId: Int,
        override val userId: Int,
        override val title: String,
        val body: String,
        val comments: List<Comment>
    ): Post

    data class AlbumPost(
        override val postId: Int,
        override val userId: Int,
        override val title: String,
        val photos: List<Photo>
    ): Post
}

