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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.pena.ismael.socialmediapager.core.composable.connectivity.ConnectivityStatus
import com.pena.ismael.socialmediapager.core.composable.preview.DarkLightPreviewUI
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.AlbumPost
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.PhotoPost
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.TextPost
import com.pena.ismael.socialmediapager.feature.postpager.screens.postlist.components.DownloadAlbumAlert
import com.pena.ismael.socialmediapager.feature.postpager.screens.postlist.components.PostListItem
import com.pena.ismael.socialmediapager.feature.postpager.screens.postlist.components.PostShimmer
import com.pena.ismael.socialmediapager.ui.theme.SocialMediaPagerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostListScreen(
    viewModel: PostListViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val textPosts = viewModel.textPosts.collectAsStateWithLifecycle()
    val albumPosts = viewModel.albumPosts.collectAsStateWithLifecycle()
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
        DownloadAlbumAlert(
            album = album,
            onDialogDismissRequest = viewModel::onDialogDismissRequest,
            onDownloadAlbum = {
                viewModel.onDownloadAlbum(album)
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = rememberLazyListState(),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PostShimmer()
                        PostShimmer()
                        PostShimmer()
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

