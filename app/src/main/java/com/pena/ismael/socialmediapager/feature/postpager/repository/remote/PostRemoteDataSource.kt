package com.pena.ismael.socialmediapager.feature.postpager.repository.remote

import com.pena.ismael.socialmediapager.core.networking.NetworkResult
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.AlbumDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.CommentDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PhotoDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PostDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(
    private val postApi: PostApi,
) {
    suspend fun fetchPaginatedTextPosts(
        startIndex: Int,
        amountPerPage: Int,
    ): NetworkResult<List<PostDto>> {
        return try {
            NetworkResult.Success(
                withContext(Dispatchers.IO) {
                    return@withContext (0 until amountPerPage).map { counter ->
                        val currentId = startIndex + counter
                        async { postApi.getPost(currentId) }.await()
                    }
                }
            )
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = e.message,
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error(
                message = e.message,
            )
        } catch (e: Exception) {
            NetworkResult.Error(
                message = e.message,
            )
        }
    }

    suspend fun fetchCommentsForPost(
        postId: Int,
    ): NetworkResult<List<CommentDto>> {
        return try {
            NetworkResult.Success(postApi.getComments(postId))
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = e.message,
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error(
                message = e.message,
            )
        } catch (e: Exception) {
            NetworkResult.Error(
                message = e.message,
            )
        }
    }

    suspend fun fetchPaginatedAlbumPosts(
        startIndex: Int,
        amountPerPage: Int,
    ): NetworkResult<List<AlbumDto>> {
        return try {
            NetworkResult.Success(
                withContext(Dispatchers.IO) {
                    return@withContext (0 until amountPerPage).map { counter ->
                        val currentId = startIndex + counter
                        async { postApi.getAlbum(currentId) }.await()
                    }
                }
            )
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = e.message,
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error(
                message = e.message,
            )
        } catch (e: Exception) {
            NetworkResult.Error(
                message = e.message,
            )
        }
    }

    suspend fun fetchPhotosForAlbum(albumId: Int): NetworkResult<List<PhotoDto>> {
        return try {
            NetworkResult.Success(
                postApi.getPhotos(albumId = albumId)
            )
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = e.message,
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error(
                message = e.message,
            )
        } catch (e: Exception) {
            NetworkResult.Error(
                message = e.message,
            )
        }
    }
}

