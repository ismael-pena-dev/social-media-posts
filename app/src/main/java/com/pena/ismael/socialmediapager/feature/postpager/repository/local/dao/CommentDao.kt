package com.pena.ismael.socialmediapager.feature.postpager.repository.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Query("SELECT * FROM CommentEntity WHERE post_id == :postId")
    suspend fun getCommentsForPost(postId: Int): List<CommentEntity>

    @Query("SELECT * FROM CommentEntity WHERE post_id == :postId")
    fun getCommentsFlow(postId: Int): Flow<List<CommentEntity>>

    @Upsert
    suspend fun upsertComments(comments: List<CommentEntity>)
}