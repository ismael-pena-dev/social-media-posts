package com.pena.ismael.socialmediapager.feature.postpager.screens.postdetail

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pena.ismael.socialmediapager.core.composable.connectivity.ConnectivityStatus
import com.pena.ismael.socialmediapager.feature.postpager.screens.postlist.components.PostListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    val textPost = viewModel.post.collectAsStateWithLifecycle(null)
    val comments = viewModel.comments.collectAsStateWithLifecycle(emptyList())
    val errorMessage = viewModel.errorMessage.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val connectivity = viewModel.connectivityStatusObserver.collectAsStateWithLifecycle(initialValue = viewModel.getCurrentConnectivityStatus())

    val context = LocalContext.current
    LaunchedEffect(key1 = errorMessage.value) {
        if (errorMessage.value != null) {
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            viewModel.onErrorMessageShown()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 32.dp, horizontal = 16.dp),
        ) {

            item {
                ConnectivityStatus(
                    status = connectivity.value,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                val post = textPost.value
                if (post == null) {
                    // TODO: Loading
                } else {
                    PostListItem(
                        post = post,
                    )
                }
            }

            items(
                items = comments.value,
                key = { comment ->
                    comment.id
                }
            ) { comment ->
                PostListItem(
                    post = comment,
                    modifier = Modifier.padding(start = 32.dp)
                )
            }

            if(isLoading.value) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}