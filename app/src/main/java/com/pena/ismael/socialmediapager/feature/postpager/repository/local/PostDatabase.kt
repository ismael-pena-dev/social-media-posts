package com.pena.ismael.socialmediapager.feature.postpager.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.dao.AlbumDao
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.dao.CommentDao
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.dao.PhotoDao
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.dao.PostDao
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.AlbumEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.CommentEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PhotoEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity

@Database(
    entities = [PostEntity::class, CommentEntity::class, AlbumEntity::class, PhotoEntity::class],
    version = 1
)
abstract class PostDatabase: RoomDatabase() {

    abstract val postDao: PostDao
    abstract val commentDao: CommentDao
    abstract val albumDao: AlbumDao
    abstract val photoDao: PhotoDao

    companion object {
        const val POST_DATABASE = "post_db"
    }

}