package com.pena.ismael.socialmediapager.feature.postpager.screens.postlist

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.pena.ismael.socialmediapager.core.composable.preview.DarkLightPreviewUI
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.AlbumPost
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.PhotoPost
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.TextPost
import com.pena.ismael.socialmediapager.feature.postpager.screens.postlist.components.PostListItem
import com.pena.ismael.socialmediapager.ui.theme.SocialMediaPagerTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    viewModel: PostListViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val textPosts = viewModel.textPosts.collectAsStateWithLifecycle(emptyList())
    val albumPosts = viewModel.albumPosts.collectAsStateWithLifecycle(emptyList())
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = viewModel.errorMessage.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val connectivity = viewModel.connectivityStatus.collectAsStateWithLifecycle(viewModel.getCurrentConnectivityStatus())

    val context = LocalContext.current
    LaunchedEffect(key1 = errorMessage.value) {
        if (errorMessage.value != null) {
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            viewModel.onErrorMessageShown()
        }
    }

    LaunchedEffect(key1 = uiState.value.navigateToRoute) {
        val route = uiState.value.navigateToRoute
        if (route != null) {
            navController.navigate(route)
            viewModel.onNavigationHandled()
        }
    }

    if (uiState.value.showDialogForDownloadingAlbum != null) {
        val album = uiState.value.showDialogForDownloadingAlbum
        AlertDialog(
            onDismissRequest = viewModel::onDialogDismissRequest
        ) {
            Card {
                Column(
                    Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Download ${album?.photos?.size} photos to storage?",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(text = "Album: (${album?.id}) ${album?.title}")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            onClick = viewModel::onDialogDismissRequest
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(Modifier.width(16.dp))
                        TextButton(
                            onClick = {
                                viewModel.onDialogDismissRequest()
                                viewModel.onDownloadAlbum(album)
                            }
                        ) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
    }

    val scrollState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollState,
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Text posts:${textPosts.value.size}",
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Text(
                        text = "Album posts:${albumPosts.value.size}",
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            item {
                ConnectivityStatus(
                    status = connectivity.value,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                )
            }

            items(
                count = textPosts.value.size,
                key = { index ->
                    textPosts.value[index].id
                }
            ) { index ->
                val textPost = textPosts.value.getOrNull(index)
                if (textPost != null) {
                    PostListItem(
                        post = textPost,
                        onClick = viewModel::onPostClick,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                val albumPost = albumPosts.value.getOrNull(index)
                if (albumPost != null) {
                    PostListItem(
                        post = albumPost,
                        onClick = viewModel::onPostClick,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
                
                if (index == textPosts.value.lastIndex) {
                    viewModel.fetchNextPosts()
                }
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

@DarkLightPreviewUI
@Composable
fun PreviewPostListScreen() {
    SocialMediaPagerTheme {
        Surface {
            val posts = listOf(
                TextPost(
                    id = 1,
                    userId = (1..10).random(),
                    title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    body = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto",
                ),
                AlbumPost(
                    id = 1,
                    userId = (1..10).random(),
                    title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    photos = listOf(
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                    ),
                ),
                TextPost(
                    id = 1,
                    userId = (1..10).random(),
                    title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    body = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto",
                )
            )
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = posts) { post ->
                    PostListItem(
                        post = post,
                    )
                }
            }
        }
    }
}

