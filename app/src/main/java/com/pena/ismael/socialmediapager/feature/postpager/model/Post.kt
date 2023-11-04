package com.pena.ismael.socialmediapager.feature.postpager.model



sealed interface Post {
    val id: Int

    data class TextPost(
        override val id: Int,
        val userId: Int,
        val title: String,
        val body: String,
    ): Post

    data class CommentPost(
        override val id: Int,
        val name: String,
        val email: String,
        val body: String,
    ): Post

    data class AlbumPost(
        override val id: Int,
        val userId: Int,
        val title: String,
        val photos: List<PhotoPost>
    ): Post

    data class PhotoPost(
        override val id: Int,
        val title: String,
        val url: String,
        val thumbnailUrl: String,
    ): Post
}

