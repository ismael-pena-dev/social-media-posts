package com.pena.ismael.socialmediapager.feature.postpager.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.repository.DtoToEntityMapper.toPostEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.EntityToModelMapper.toTextPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.AlbumEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.CommentEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PhotoEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.PostRemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {
    val posts = postRepository.postsFlow

    init {
        loadInitial()
    }

    private fun loadInitial() {
        viewModelScope.launch {
            postRepository.loadInitial(pageSize = PAGE_SIZE)
        }
    }

    fun fetchNextPosts() {
        viewModelScope.launch {
            postRepository.fetchNextPosts(PAGE_SIZE)
        }
    }
    
    fun onPostClick(post: Post) {
        // TODO: Navigate
    }

    companion object {
        private const val PAGE_SIZE = 5
    }
}

class PostRepository @Inject constructor(
    private val local: PostLocalDataSource,
    private val remote: PostRemoteDataSource,
) {
    private val textPostsFlow = local.textPostsFlow
    val postsFlow = textPostsFlow

    suspend fun fetchNextPosts(pageSize: Int) {
        val lastIndex = local.getLastIndex()
        try {
            val fetchedTextPosts = remote.fetchPaginatedTextPosts(startIndex = lastIndex + 1, amountPerPage = pageSize)
            val textPostsEntities = fetchedTextPosts.map { it.toPostEntity() }
            local.insertTextPosts(textPostsEntities)
        } catch (e: HttpException) {
            // TODO
        } catch (e: IOException) {
            // TODO
        } catch (e: Exception) {
            // TODO
        }
    }

    suspend fun loadInitial(pageSize: Int) {
        val cacheCount = local.getLastIndex()
        if (cacheCount == 0) {
            fetchNextPosts(pageSize)
        }
    }
}

class PostLocalDataSource @Inject constructor(
    private val postDb: PostDatabase
) {
    val textPostsFlow = postDb.postDao.getAll().map { entityList ->
        entityList.map {  postEntity ->
            postEntity.toTextPost()
        }
    }
    suspend fun getLastIndex(): Int {
        return postDb.postDao.count()
    }

    suspend fun insertTextPosts(textPosts: List<PostEntity>) {
        postDb.postDao.insertAll(textPosts)
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




















