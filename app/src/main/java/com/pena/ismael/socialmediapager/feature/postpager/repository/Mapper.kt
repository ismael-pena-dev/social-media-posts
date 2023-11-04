package com.pena.ismael.socialmediapager.feature.postpager.repository

import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.AlbumEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.CommentEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PhotoEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.AlbumDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.CommentDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PhotoDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PostDto

object DtoToEntityMapper {
    fun PostDto.toPostEntity(): PostEntity {
        return PostEntity(
            postId = id,
            userId = userId,
            title = title,
            body = body
        )
    }

    fun CommentDto.toCommentEntity(): CommentEntity {
        return CommentEntity(
            commentId = id,
            postId = postId,
            name = name,
            email = email,
            body = body
        )
    }

    fun AlbumDto.toAlbumEntity(): AlbumEntity {
        return AlbumEntity(
            albumId = id,
            userId = userId,
            title = title
        )
    }

    fun PhotoDto.toPhotoEntity(): PhotoEntity {
        return PhotoEntity(
            photoId = id,
            albumId = albumId,
            title = title,
            url = url,
            thumbnailUrl = thumbnailUrl
        )
    }
}

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