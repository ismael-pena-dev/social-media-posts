package com.pena.ismael.socialmediapager.feature.postpager.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pena.ismael.socialmediapager.feature.postpager.screens.postdetail.PostDetailScreen
import com.pena.ismael.socialmediapager.feature.postpager.screens.postlist.PostListScreen

@Composable
fun PostNavHost(
    navController: NavHostController
) {
    val animationSpeed by remember {
        mutableIntStateOf(700)
    }
    NavHost(
        navController = navController,
        startDestination = "posts",
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            route = PostNavRoute.PostList.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = animationSpeed)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = animationSpeed)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = animationSpeed)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = animationSpeed)
                )
            },
        ) {
            PostListScreen(
                navController = navController
            )
        }
        composable(
            route = PostNavRoute.PostDetails.route,
            arguments = listOf(navArgument("post_id") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = animationSpeed)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = animationSpeed)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = animationSpeed)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = animationSpeed)
                )
            },
        ) {
            PostDetailScreen()
        }
    }
}