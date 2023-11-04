package com.pena.ismael.socialmediapager.feature.postpager.postlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pena.ismael.socialmediapager.R
import com.pena.ismael.socialmediapager.core.DarkLightPreview
import com.pena.ismael.socialmediapager.core.DarkLightPreviewUI
import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.AlbumPost
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.PhotoPost
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.TextPost
import com.pena.ismael.socialmediapager.ui.theme.SocialMediaPagerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostListScreen(
    viewModel: PostListViewModel = hiltViewModel()
) {
    val textPosts = viewModel.textPosts.collectAsStateWithLifecycle(emptyList())
    val albumPosts = viewModel.albumPosts.collectAsStateWithLifecycle(emptyList())

//    val context = LocalContext.current
//    LaunchedEffect(key1 = posts.loadState) {
//        val loadState = posts.loadState.refresh
//        if (loadState is LoadState.Error) {
//            Toast.makeText(context, "Error: ${loadState.error.message}", Toast.LENGTH_SHORT).show()
//        }
//    }

    val scrollState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollState,
        ) {
            stickyHeader() {
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
                        onClick = viewModel::onPostClick
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                val albumPost = albumPosts.value.getOrNull(index)
                if (albumPost != null) {
                    PostListItem(
                        post = albumPost,
                        onClick = viewModel::onPostClick
                    )
                }
                
                if (index == textPosts.value.lastIndex) {
                    viewModel.fetchNextPosts()
                }
            }

//            if(posts.loadState.append is LoadState.Loading || posts.loadState.refresh is LoadState.Loading) {
//                item {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                }
//            }
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

@Composable
fun PostListItem(
    post: Post,
    modifier: Modifier = Modifier,
    onClick: (Post) -> Unit = {}
) {
    Card(
        modifier = modifier
            .clickable {
                onClick(post)
            }
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        when (post) {
            is TextPost -> TextPostListItemContent(post = post, modifier = Modifier
                .padding(vertical = 24.dp)
                .padding(horizontal = 16.dp))
            is AlbumPost -> AlbumPostListItemContent(post = post, modifier = Modifier
                .padding(vertical = 24.dp)
                .padding(horizontal = 16.dp))
            else -> {
                // TODO
            }
        }
    }
}

@Composable
fun UserIcon(
    userId: Int,
    modifier: Modifier = Modifier
) {
    val color = remember {
        mutableLongStateOf(
            when (userId) {
                1 -> 0xFFEF5350
                2 -> 0xFFAB47BC
                3 -> 0xFF5C6BC0
                4 -> 0xFF29B6F6
                5 -> 0xFF26A69A
                6 -> 0xFF66BB6A
                7 -> 0xFFD4E157
                8 -> 0xFFFFCA28
                9 -> 0xFF8D6E63
                else -> 0xFF78909C
            }
        )
    }
    Box(modifier = Modifier.size(48.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = userId.toString(),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(48.dp),
        )
        Box(
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape)
                .alpha(.7F)
                .background(color = Color(color.value))
        )
    }
}

@Composable
fun TextPostListItemContent(
    post: TextPost,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        UserIcon(userId = post.userId)
        Column {
            Text(
                text =  "(${post.id}) " + post.title.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.body.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@DarkLightPreview
@Composable
fun PreviewTextPostListItem() {
    SocialMediaPagerTheme {
        Surface {
            PostListItem(
                post = TextPost(
                    id = 1,
                    userId = (1..10).random(),
                    title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    body = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto",
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlbumPostListItemContent(
    post: AlbumPost,
    modifier: Modifier = Modifier,
    previewLimit: Int = 3
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        UserIcon(userId = post.userId)
        Column {
            Text(
                text = "(${post.id}) " + post.title.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                post.photos
                    .take(previewLimit)
                    .forEach {  photo ->
                        AsyncImage(
                            modifier = Modifier.size(60.dp),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photo.thumbnailUrl)
                                .crossfade(true)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .build(),
                            contentDescription = photo.title
                        )
                }
                if (post.photos.size > previewLimit) {
                    Text(text = "(+${post.photos.size - previewLimit})")
                }
            }
        }
    }
}

@DarkLightPreview
@Composable
fun PreviewAlbumPostListItem() {
    SocialMediaPagerTheme {
        Surface {
            PostListItem(
                post = AlbumPost(
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
                )
            )
        }
    }
}
