package com.pena.ismael.socialmediapager.feature.postpager.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.repository.EntityToModelMapper.toTextPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.AlbumEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.CommentEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PhotoEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {
    val posts = postRepository.textPosts.cachedIn(viewModelScope)
    
    fun onPostClick(post: Post) {
        // TODO: Navigate
    }
}

class PostRepository @Inject constructor(
    private val textPostPager: Pager<Int, PostEntity>
) {
    val textPosts = textPostPager.flow.map {  pagingData ->
        pagingData.map { postEntity ->
            postEntity.toTextPost()
        }
    }
}

@Database(
    entities = [PostEntity::class, CommentEntity::class, AlbumEntity::class, PhotoEntity::class], 
    version = 1
)
abstract class PostDatabase: RoomDatabase() {
    
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun albumDao(): AlbumDao
    abstract fun photoDao(): PhotoDao

    companion object {
        const val POST_DATABASE = "post_db"
    }
    
}

@Dao
interface PostDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Query("SELECT * FROM PostEntity")
    fun pagingSource(): PagingSource<Int, PostEntity>

    @Query("DELETE FROM PostEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(post_id) FROM PostEntity")
    suspend fun count(): Int

}

@Dao
interface CommentDao {
    
    @Query("SELECT * FROM CommentEntity WHERE post_id == :postId")
    fun getCommentsForPost(postId: Int): Flow<List<CommentEntity>>
    
    @Upsert
    suspend fun upsertComments(comments: List<CommentEntity>)
}

@Dao
interface AlbumDao {

    @Query("SELECT * FROM AlbumEntity")
    fun getAllAlbums(): Flow<List<AlbumEntity>>

    @Upsert
    suspend fun upsertAlbums(Albums: List<AlbumEntity>)
}

@Dao
interface PhotoDao {

    @Query("SELECT * FROM PhotoEntity WHERE album_id == :albumId")
    fun getPhotosForPost(albumId: Int): Flow<List<PhotoEntity>>

    @Upsert
    suspend fun upsertPhotos(Photos: List<PhotoEntity>)
}



















