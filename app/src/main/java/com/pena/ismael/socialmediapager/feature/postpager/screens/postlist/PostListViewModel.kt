package com.pena.ismael.socialmediapager.feature.postpager.screens.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pena.ismael.socialmediapager.core.services.downloadmanager.Downloader
import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.navigation.PostNavRoute
import com.pena.ismael.socialmediapager.feature.postpager.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val fileDownloader: Downloader,
): ViewModel() {
    private val _uiState = MutableStateFlow(PostListUiState())
    val uiState: StateFlow<PostListUiState>
        get() = _uiState

    val textPosts = postRepository.textPostsFlow
    val albumPosts = postRepository.albumPostsFlow
    val errorMessage = postRepository.errorMessage
    val isLoading = postRepository.isLoading

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
        if (post is Post.TextPost) {
            val navRoute = PostNavRoute.PostDetails.buildNavRoute(postId = post.id)
            _uiState.value = _uiState.value.copy(navigateToRoute = navRoute)
        }
        if (post is Post.AlbumPost) {
            _uiState.value = _uiState.value.copy(showDialogForDownloadingAlbum = post)
        }
    }

    fun onErrorMessageShown() {
        postRepository.onErrorConsumed()
    }

    fun onDialogDismissRequest() {
        _uiState.value = _uiState.value.copy(showDialogForDownloadingAlbum = null)
    }

    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(navigateToRoute = null)
    }

    fun onDownloadAlbum(album: Post.AlbumPost?) {
        if (album == null || album.photos.isEmpty()) {
            return
        }

        album.photos.forEach { photo ->
            fileDownloader.downloadImage(
                url = photo.url,
                fileName = photo.title
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 5
    }
}




















