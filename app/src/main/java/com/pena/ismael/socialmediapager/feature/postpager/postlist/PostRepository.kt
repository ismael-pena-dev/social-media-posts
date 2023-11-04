package com.pena.ismael.socialmediapager.feature.postpager.postlist

import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.repository.DtoToEntityMapper.toAlbumEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.DtoToEntityMapper.toCommentEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.DtoToEntityMapper.toPhotoEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.DtoToEntityMapper.toPostEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.EntityToModelMapper.toAlbumPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.EntityToModelMapper.toCommentPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.EntityToModelMapper.toPhotoPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.EntityToModelMapper.toTextPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.CommentEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.PostRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val local: PostLocalDataSource,
    private val remote: PostRemoteDataSource,
) {
    val textPostsFlow = local.textPostsFlow
        .map { entityList ->
            entityList.map {  postEntity ->
                postEntity.toTextPost()
            }
        }

    private val albumDbFlow = local.albumPostsFlow
    private val photoDbFlow = local.photoPostsFlow
    val albumPostsFlow = combine(albumDbFlow, photoDbFlow) { albumEntities, photoEntities ->
        albumEntities.map { albumEntity ->
            val photos = photoEntities
                .filter {
                    it.albumId == albumEntity.albumId
                }
                .map {
                    it.toPhotoPost()
                }
            albumEntity.toAlbumPost(photos = photos)
        }
    }

    suspend fun fetchNextPosts(pageSize: Int) {
        val lastIndex = local.getLastIndex()
        try {
            withContext(Dispatchers.IO) {
                // TextPosts
                launch {
                    val fetchedTextPosts = remote.fetchPaginatedTextPosts(
                        startIndex = lastIndex + 1,
                        amountPerPage = pageSize
                    )
                    local.insertTextPosts(textPosts = fetchedTextPosts.map { it.toPostEntity() })
                }

                // AlbumPosts
                launch {
                    val fetchedAlbumPosts = remote.fetchPaginatedAlbumPosts(
                        startIndex = lastIndex + 1,
                        amountPerPage = pageSize
                    )
                    launch {
                        local.insertAlbumPosts(albumPosts = fetchedAlbumPosts.map { it.toAlbumEntity() })
                    }
                    launch {
                        fetchedAlbumPosts.forEach { album ->
                            launch {
                                val fetchedPhotos = remote.fetchPhotosForAlbum(albumId = album.id)
                                local.insertPhotoPosts(fetchedPhotos.map { it.toPhotoEntity() })
                            }
                        }
                    }
                }
            }
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

    fun getTextPostFlow(postId: Int): Flow<Post.TextPost> {
        return local.getTextPost(postId).map {
            it.toTextPost()
        }
    }

    suspend fun getCommentsForPostFlow(postId: Int): Flow<List<Post.CommentPost>> {
        withContext(Dispatchers.IO) {
            launch {
                // Fetch and cache remote only if it isn't cached already
                val cachedComments = local.getCommentsForPost(postId)
                if (cachedComments.isEmpty()) {
                    val fetchedComments = remote.fetchCommentsForPost(postId)
                    local.insertComments(fetchedComments.map { it.toCommentEntity() })
                }
            }
        }
        return local.getCommentsFlow(postId).map {  commentList ->
            commentList.map {  comment ->
                comment.toCommentPost()
            }
        }
    }
}