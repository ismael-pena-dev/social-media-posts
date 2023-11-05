package com.pena.ismael.socialmediapager.feature.postpager.repository

import com.pena.ismael.socialmediapager.core.networking.NetworkResult
import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.PostLocalDataSource
import com.pena.ismael.socialmediapager.feature.postpager.repository.mappers.DtoToEntityMapper.toAlbumEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.mappers.DtoToEntityMapper.toCommentEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.mappers.DtoToEntityMapper.toPhotoEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.mappers.DtoToEntityMapper.toPostEntity
import com.pena.ismael.socialmediapager.feature.postpager.repository.mappers.EntityToModelMapper.toAlbumPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.mappers.EntityToModelMapper.toCommentPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.mappers.EntityToModelMapper.toPhotoPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.mappers.EntityToModelMapper.toTextPost
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.PostRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?>
        get() = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

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
        withContext(Dispatchers.IO) {
            // TextPosts
            launch {
                setIsLoading(true)
                val networkResult = remote.fetchPaginatedTextPosts(
                    startIndex = lastIndex + 1,
                    amountPerPage = pageSize
                )
                setIsLoading(false)

                when (networkResult) {
                    is NetworkResult.Success -> {
                        val fetchedTextPosts = networkResult.data
                        local.insertTextPosts(textPosts = fetchedTextPosts.map { it.toPostEntity() })
                    }
                    is NetworkResult.Error -> {
                        setErrorMessage(networkResult.message)
                    }
                }
            }

            // AlbumPosts
            launch {
                setIsLoading(true)
                val fetchAlbumsResult = remote.fetchPaginatedAlbumPosts(
                    startIndex = lastIndex + 1,
                    amountPerPage = pageSize
                )
                setIsLoading(false)

                when (fetchAlbumsResult) {
                    is NetworkResult.Success -> {
                        val fetchedAlbumPosts = fetchAlbumsResult.data
                        launch {
                            local.insertAlbumPosts(albumPosts = fetchedAlbumPosts.map { it.toAlbumEntity() })
                        }
                        launch {
                            fetchedAlbumPosts.forEach { album ->
                                launch {
                                    setIsLoading(true)
                                    val fetchPhotosResult = remote.fetchPhotosForAlbum(albumId = album.id)
                                    setIsLoading(true)
                                    when (fetchPhotosResult) {
                                        is NetworkResult.Success -> {
                                            val fetchedPhotos = fetchPhotosResult.data
                                            local.insertPhotoPosts(fetchedPhotos.map { it.toPhotoEntity() })
                                        }
                                        is NetworkResult.Error -> {
                                            setErrorMessage(fetchPhotosResult.message)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        setErrorMessage(fetchAlbumsResult.message)
                    }
                }
            }
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
                    setIsLoading(true)
                    val networkResult = remote.fetchCommentsForPost(postId)
                    setIsLoading(false)
                    when (networkResult) {
                        is NetworkResult.Success -> {
                            val fetchedComments = networkResult.data
                            local.insertComments(fetchedComments.map { it.toCommentEntity() })
                        }
                        is NetworkResult.Error -> {
                            setErrorMessage(networkResult.message)
                        }
                    }
                }
            }
        }
        return local.getCommentsFlow(postId).map {  commentList ->
            commentList.map {  comment ->
                comment.toCommentPost()
            }
        }
    }

    fun onErrorConsumed() {
        setErrorMessage(null)
    }

    private fun setErrorMessage(error: String?) {
        _errorMessage.value = error
    }

    private fun setIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}