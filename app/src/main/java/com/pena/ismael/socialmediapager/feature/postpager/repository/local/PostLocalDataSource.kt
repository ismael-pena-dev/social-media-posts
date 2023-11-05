package com.pena.ismael.socialmediapager.feature.postpager.repository.local

import com.pena.ismael.socialmediapager.feature.postpager.repository.local.PostDatabase
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.AlbumEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.CommentEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PhotoEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostLocalDataSource @Inject constructor(
    private val postDb: PostDatabase
) {
    val textPostsFlow = postDb.postDao.getAll()

    val albumPostsFlow = postDb.albumDao.getAllAlbums()

    val photoPostsFlow = postDb.photoDao.getAllPhotos()

    suspend fun getLastIndex(): Int {
        return postDb.postDao.count()
    }

    suspend fun insertTextPosts(
        textPosts: List<PostEntity>,
    ) {
        postDb.postDao.insertAll(textPosts)
    }

    suspend fun insertAlbumPosts(
        albumPosts: List<AlbumEntity>
    ) {
        postDb.albumDao.upsertAlbums(albumPosts)
    }

    suspend fun insertPhotoPosts(
        photoPosts: List<PhotoEntity>
    ) {
        postDb.photoDao.upsertPhotos(photoPosts)
    }

    fun getTextPost(postId: Int): Flow<PostEntity> {
        return postDb.postDao.getPost(postId)
    }

    suspend fun getCommentsForPost(postId: Int): List<CommentEntity> {
        return postDb.commentDao.getCommentsForPost(postId)
    }

    fun getCommentsFlow(postId: Int): Flow<List<CommentEntity>> {
        return postDb.commentDao.getCommentsFlow(postId)
    }

    suspend fun insertComments(comments: List<CommentEntity>) {
        postDb.commentDao.upsertComments(comments)
    }
}


