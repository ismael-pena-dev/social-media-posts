package com.pena.ismael.socialmediapager.feature.postpager.repository.remote

import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.AlbumDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.CommentDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PhotoDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PostDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(
    private val postApi: PostApi,
) {
    suspend fun fetchPaginatedTextPosts(
        startIndex: Int,
        amountPerPage: Int,
    ): List<PostDto> {
        return withContext(Dispatchers.IO) {
            return@withContext (0 until amountPerPage).map { counter ->
                val currentId = startIndex + counter
                async { postApi.getPost(currentId) }.await()
            }
        }
    }

    suspend fun fetchCommentsForPost(
        postId: Int,
    ): List<CommentDto> {
        return postApi.getComments(postId)
    }

    suspend fun fetchPaginatedAlbumPosts(
        startIndex: Int,
        amountPerPage: Int,
    ): List<AlbumDto> {
        return withContext(Dispatchers.IO) {
            return@withContext (0 until amountPerPage).map { counter ->
                val currentId = startIndex + counter
                async { postApi.getAlbum(currentId) }.await()
            }
        }
    }

    suspend fun fetchPhotosForAlbum(albumId: Int): List<PhotoDto> {
        return postApi.getPhotos(albumId = albumId)
    }
}