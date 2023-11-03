package com.pena.ismael.socialmediapager.feature.postpager.postlist

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.paging.util.getClippedRefreshKey
import androidx.room.withTransaction
import com.pena.ismael.socialmediapager.feature.postpager.repository.DtoToEntityMapper.toPostEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.PostRemoteDataSource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val postDatabase: PostDatabase,
    private val postRemoteDataSource: PostRemoteDataSource,
): RemoteMediator<Int, PostEntity>() {

    override suspend fun initialize(): InitializeAction {
        val count = postDatabase.postDao.count()
        Log.d(TAG, "initialize() -> db.count = $count")
        if (count > 0)
            return InitializeAction.SKIP_INITIAL_REFRESH

        return super.initialize()
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        Log.d(TAG, "load(loadType:$loadType, \nstate:$state)")
        return try {
            val loadKey  = when(loadType) {
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()?.postId ?: 0
                    lastItem + 1
//                    if (lastItem == null) {
//                        // No items were loaded after the initial REFRESH and there's no more items to load
//                        return MediatorResult.Success(endOfPaginationReached = false)
//                    } else {
//                        lastItem.postId + 1
//                    }
                }
                LoadType.REFRESH -> 1 // Start from the beginning
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            }

            // Remote
            val posts = postRemoteDataSource.getPaginatedPosts(
                startIndex = loadKey,
                amountPerPage = state.config.pageSize
            )
            val isEndOfPaginationReached = posts.size < state.config.pageSize // Remote call returns empty list when there's no more to fetch
            Log.d(TAG, "loadKey: $loadKey\nposts: ${posts.map { it.id }} -> size:${posts.size}\nisEndOfPaginationReached: $isEndOfPaginationReached")

            Log.d(TAG, "Before transaction: ${postDatabase.postDao.count()}")
            // DB
            postDatabase.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    postDatabase.postDao().deleteAll() // Delete outdated entries
//                }

                // Insert into database, which invalidates the current PagingData, allowing Paging to present the updates in the DB
                val postEntities = posts.map {  postDto ->
                    postDto.toPostEntity()
                }
                postDatabase.postDao.insertAll(postEntities)
            }
            Log.d(TAG, "After transaction: ${postDatabase.postDao.count()}")
            state.anchorPosition

            MediatorResult.Success(
                endOfPaginationReached = isEndOfPaginationReached
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        private val TAG = PostRemoteMediator::javaClass.name
    }
}