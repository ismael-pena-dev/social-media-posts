package com.pena.ismael.socialmediapager.feature.postpager.repository.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM AlbumEntity")
    fun getAllAlbums(): Flow<List<AlbumEntity>>

    @Upsert
    suspend fun upsertAlbums(albums: List<AlbumEntity>)

    @Query("DELETE FROM AlbumEntity")
    suspend fun deleteAll()
}