package com.pena.ismael.socialmediapager.feature.postpager.repository.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM PhotoEntity")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM PhotoEntity WHERE album_id == :albumId")
    fun getPhotosForAlbum(albumId: Int): Flow<List<PhotoEntity>>

    @Upsert
    suspend fun upsertPhotos(photos: List<PhotoEntity>)

    @Query("DELETE FROM PhotoEntity")
    suspend fun deleteAll()
}