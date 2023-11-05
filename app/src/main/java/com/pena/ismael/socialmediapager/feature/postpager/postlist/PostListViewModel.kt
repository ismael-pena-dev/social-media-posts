package com.pena.ismael.socialmediapager.feature.postpager.postlist

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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
            _uiState.value = _uiState.value.copy(navigateToRoute = "posts/${post.id}")
        }
        if (post is Post.AlbumPost) {
            _uiState.value = _uiState.value.copy(showDialogForDownloadingAlbum = post)
        }
    }

    fun onErrorMessageShown() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
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

data class PostListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDialogForDownloadingAlbum: Post.AlbumPost? = null,
    val navigateToRoute: String? = null,
)

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val postId: Int = checkNotNull(savedStateHandle["post_id"])

    private val _post = MutableStateFlow<Post.TextPost?>(null)
    val post: StateFlow<Post.TextPost?>
        get() = _post

    private val _comments = MutableStateFlow<List<Post.CommentPost>>(emptyList())
    val comments: StateFlow<List<Post.CommentPost>>
        get() = _comments

    init {
        viewModelScope.launch {
            postRepository.getTextPostFlow(postId).collectLatest { post ->
                _post.value = post
            }
        }
        viewModelScope.launch {
            postRepository.getCommentsForPostFlow(postId).collectLatest { comments ->
                _comments.value = comments
            }
        }
    }
}

interface Downloader {
    fun downloadImage(url: String, fileName: String): Long
}

class AndroidDownloader @Inject constructor(
    @ApplicationContext private val context: Context
): Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun downloadImage(url: String, fileName: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("${fileName}.jpeg")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "image.jpg")
        return downloadManager.enqueue(request)
    }

}




















