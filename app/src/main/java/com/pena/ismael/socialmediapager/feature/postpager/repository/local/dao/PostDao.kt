package com.pena.ismael.socialmediapager.feature.postpager.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    // For this use case, posts are not editable
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Query("SELECT * FROM PostEntity")
    fun getAll(): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity WHERE post_id == :postId")
    fun getPost(postId: Int): Flow<PostEntity>

    @Query("DELETE FROM PostEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(post_id) FROM PostEntity")
    suspend fun count(): Int

}