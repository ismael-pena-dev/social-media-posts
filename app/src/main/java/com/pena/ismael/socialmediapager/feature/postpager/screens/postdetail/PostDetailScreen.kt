package com.pena.ismael.socialmediapager.feature.postpager.screens.postdetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pena.ismael.socialmediapager.feature.postpager.screens.postlist.components.PostListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    val textPost = viewModel.post.collectAsStateWithLifecycle(null)
    val comments = viewModel.comments.collectAsStateWithLifecycle(emptyList())

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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = scrollState,
            contentPadding = PaddingValues(vertical = 32.dp, horizontal = 16.dp),
        ) {
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