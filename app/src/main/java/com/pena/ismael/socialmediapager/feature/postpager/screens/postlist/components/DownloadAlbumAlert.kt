package com.pena.ismael.socialmediapager.feature.postpager.screens.postlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pena.ismael.socialmediapager.core.composable.preview.DarkLightPreview
import com.pena.ismael.socialmediapager.feature.postpager.model.Post
import com.pena.ismael.socialmediapager.ui.theme.SocialMediaPagerTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DownloadAlbumAlert(
    album: Post.AlbumPost?,
    onDialogDismissRequest: () -> Unit = {},
    onDownloadAlbum: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDialogDismissRequest
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
                        onClick = onDialogDismissRequest
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(Modifier.width(16.dp))
                    TextButton(
                        onClick = {
                            onDialogDismissRequest()
                            onDownloadAlbum()
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}

@DarkLightPreview
@Composable fun PreviewDownloadAlbumAlert() {
    val album = Post.AlbumPost(
        id = 1,
        userId = 1,
        title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        photos = listOf(
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
            Post.PhotoPost(id = 52, title = "non sunt voluptatem placeat consequuntur rem incidunt", url = "https://via.placeholder.com/600/8e973b", thumbnailUrl = "https://via.placeholder.com/150/8e973b"),
        ),
    )
    SocialMediaPagerTheme {
        Surface {
            DownloadAlbumAlert(
                album,
            )
        }
    }
}