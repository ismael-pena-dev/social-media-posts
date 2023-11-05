package com.pena.ismael.socialmediapager.feature.postpager.repository.mappers

import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.AlbumEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.CommentEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PhotoEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity

object EntityToModelMapper {
    fun PostEntity.toTextPost(): Post.TextPost {
        return Post.TextPost(
            id = postId,
            userId = userId,
            title = title,
            body = body,
        )
    }

    fun CommentEntity.toCommentPost(): Post.CommentPost {
        return Post.CommentPost(
            id = commentId,
            name = name,
            email = email,
            body = body
        )
    }

    fun AlbumEntity.toAlbumPost(photos: List<Post.PhotoPost> = emptyList()): Post.AlbumPost {
        return Post.AlbumPost(
            id = albumId,
            userId = userId,
            title = title,
            photos = photos
        )
    }

    fun PhotoEntity.toPhotoPost(): Post.PhotoPost {
        return Post.PhotoPost(
            id = photoId,
            title = title,
            url = url,
            thumbnailUrl = thumbnailUrl
        )
    }
}