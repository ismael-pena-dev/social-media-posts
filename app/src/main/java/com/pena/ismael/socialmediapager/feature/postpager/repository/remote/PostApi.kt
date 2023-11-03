package com.pena.ismael.socialmediapager.feature.postpager.repository.remote

import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.AlbumDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.CommentDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PhotoDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PostDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject

interface PostApi {

    @GET("posts/{post_id}")
    suspend fun getPost(
        @Path("post_id") postId: Int,
    ): PostDto

    @GET("posts/{post_id}/comments")
    suspend fun getComments(
        @Path("post_id") postId: Int,
    ): List<CommentDto>

    @GET("albums")
    suspend fun getAlbums(): List<AlbumDto>

    @GET("albums/{album_id}/photos")
    suspend fun getPhotos(
        @Path("album_id") albumId: Int,
    ): List<PhotoDto>

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

}

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
}