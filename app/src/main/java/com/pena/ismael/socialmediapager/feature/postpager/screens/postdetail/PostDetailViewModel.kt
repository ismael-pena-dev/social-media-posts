package com.pena.ismael.socialmediapager.feature.postpager.screens.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pena.ismael.socialmediapager.core.services.connectivityobserver.ConnectivityObserver
import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle,
    private val connectivityObserver: ConnectivityObserver,
): ViewModel() {
    private val postId: Int = checkNotNull(savedStateHandle["post_id"])

    private val _post = MutableStateFlow<Post.TextPost?>(null)
    val post: StateFlow<Post.TextPost?>
        get() = _post

    private val _comments = MutableStateFlow<List<Post.CommentPost>>(emptyList())
    val comments: StateFlow<List<Post.CommentPost>>
        get() = _comments

    val errorMessage = postRepository.errorMessage
    val isLoading = postRepository.isLoading
    val connectivityStatusObserver = connectivityObserver.observe()

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

    fun onErrorMessageShown() {
        postRepository.onErrorConsumed()
    }

    fun getCurrentConnectivityStatus(): ConnectivityObserver.Status {
        return if (connectivityObserver.isConnectionAvailable())
            ConnectivityObserver.Status.Available
        else ConnectivityObserver.Status.Unavailable
    }
}