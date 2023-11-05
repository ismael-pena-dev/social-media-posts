package com.pena.ismael.socialmediapager.feature.postpager.repository.remote

import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.AlbumDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.CommentDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PhotoDto
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.dto.PostDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PostApi {

    @GET("posts/{post_id}")
    suspend fun getPost(
        @Path("post_id") postId: Int,
    ): PostDto

    @GET("posts/{post_id}/comments")
    suspend fun getComments(
        @Path("post_id") postId: Int,
    ): List<CommentDto>

    @GET("albums/{album_id}")
    suspend fun getAlbum(
        @Path("album_id") albumId: Int
    ): AlbumDto

    @GET("albums/{album_id}/photos")
    suspend fun getPhotos(
        @Path("album_id") albumId: Int,
    ): List<PhotoDto>

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

}

