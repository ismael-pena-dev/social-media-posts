package com.pena.ismael.socialmediapager.feature.postpager.navigation

sealed class PostNavRoute(val route: String) {
    data object PostList: PostNavRoute("posts") {
        fun buildNavRoute(): String {
            return route
        }
    }
    data object PostDetails: PostNavRoute("posts/{post_id}") {
        fun buildNavRoute(
            postId: Int,
        ): String {
            return route.replace("{post_id}", postId.toString())
        }
    }
}