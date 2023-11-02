package com.pena.ismael.socialmediapager.feature.postpager.postlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pena.ismael.socialmediapager.R
import com.pena.ismael.socialmediapager.core.DarkLightPreview
import com.pena.ismael.socialmediapager.core.DarkLightPreviewUI
import com.pena.ismael.socialmediapager.feature.postpager.model.Photo
import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.AlbumPost
import com.pena.ismael.socialmediapager.feature.postpager.model.Post.TextPost
import com.pena.ismael.socialmediapager.ui.theme.SocialMediaPagerTheme

@Composable
fun PostListScreen(
    onClick: (Post) -> Unit = {}
) {
    val posts = listOf<Post>() // TODO: Get from viewmodel
}

@Composable
fun PostListScreenRaw(
    posts: List<Post>,
    onClick: (Post) -> Unit = {}
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = posts) {post ->
            PostListItem(
                post = post,
                onClick = onClick
            )
        }
    }
}

@DarkLightPreviewUI
@Composable
fun PreviewPostListScreen() {
    SocialMediaPagerTheme {
        Surface {
            PostListScreenRaw(
                posts = listOf(
                    TextPost(
                        postId = 1,
                        userId = (1..10).random(),
                        title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                        body = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto",
                        comments = emptyList()
                    ),
                    AlbumPost(
                        postId = 1,
                        userId = (1..10).random(),
                        title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                        photos = listOf(
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                            Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        ),
                    ),
                    TextPost(
                        postId = 1,
                        userId = (1..10).random(),
                        title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                        body = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto",
                        comments = emptyList()
                    )
                )
            )
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
        modifier = modifier.clickable {
            onClick(post)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        when (post) {
            is TextPost -> TextPostListItemContent(post = post, modifier = Modifier
                .padding(vertical = 24.dp)
                .padding(horizontal = 16.dp))
            is AlbumPost -> AlbumPostListItemContent(post = post, modifier = Modifier
                .padding(vertical = 24.dp)
                .padding(horizontal = 16.dp))
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
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        UserIcon(userId = post.userId)
        Column {
            Text(
                text = post.title.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.body.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light,
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
                    postId = 1,
                    userId = (1..10).random(),
                    title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    body = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto",
                    comments = emptyList()
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
    previewLimit: Int = 8
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        UserIcon(userId = post.userId)
        Column {
            Text(
                text = post.title.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                maxItemsInEachRow = 5
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
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                            .alpha(.6F),
                        painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                        contentDescription = "more"
                    )
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
                    postId = 1,
                    userId = (1..10).random(),
                    title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    photos = listOf(
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                        Photo(photoId = 52, albumId = 1, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
                    ),
                )
            )
        }
    }
}
