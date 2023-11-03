package com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoEntity(
    @PrimaryKey @ColumnInfo(name = "photo_id") val photoId: Int,
    @ColumnInfo(name = "album_id")val albumId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,
)