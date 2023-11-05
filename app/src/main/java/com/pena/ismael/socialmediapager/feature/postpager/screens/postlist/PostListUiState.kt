package com.pena.ismael.socialmediapager.feature.postpager.screens.postlist

import com.pena.ismael.socialmediapager.feature.postpager.model.Post

data class PostListUiState(
    val showDialogForDownloadingAlbum: Post.AlbumPost? = null,
    val navigateToRoute: String? = null,
)