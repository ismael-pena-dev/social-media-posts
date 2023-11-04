package com.pena.ismael.socialmediapager.feature.postpager.postlist

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
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
//        .map {  albumEntities ->
//            albumEntities.map {  album ->
//                album.toAlbumPost()
//            }
//        }

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

@Dao
interface CommentDao {

    @Query("SELECT * FROM CommentEntity WHERE post_id == :postId")
    suspend fun getCommentsForPost(postId: Int): List<CommentEntity>

    @Query("SELECT * FROM CommentEntity WHERE post_id == :postId")
    fun getCommentsFlow(postId: Int): Flow<List<CommentEntity>>

    @Upsert
    suspend fun upsertComments(comments: List<CommentEntity>)
}

@Dao
interface AlbumDao {

    @Query("SELECT * FROM AlbumEntity")
    fun getAllAlbums(): Flow<List<AlbumEntity>>

    @Upsert
    suspend fun upsertAlbums(albums: List<AlbumEntity>)

    @Query("DELETE FROM AlbumEntity")
    suspend fun deleteAll()
}

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
