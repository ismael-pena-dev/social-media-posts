package com.pena.ismael.socialmediapager

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.PostApi
import com.pena.ismael.socialmediapager.feature.postpager.postlist.PostDatabase
import com.pena.ismael.socialmediapager.feature.postpager.postlist.PostDetailScreen
import com.pena.ismael.socialmediapager.feature.postpager.postlist.PostListScreen
import com.pena.ismael.socialmediapager.ui.theme.SocialMediaPagerTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialMediaPagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    PostNavHost(
                        navController = navController
                    )
                }
            }
        }
    }
}

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
            route = "posts",
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
            route = "posts/{post_id}",
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

@Module
@InstallIn(SingletonComponent::class)
abstract class PostModule {

    companion object {
        @Provides
        @Singleton
        fun providesPostDatabase(
            @ApplicationContext context: Context
        ): PostDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = PostDatabase::class.java,
                name = PostDatabase.POST_DATABASE
            ).build()
        }

        @Provides
        @Singleton
        @Named("post")
        fun providePostRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(PostApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun providePostApi(
            @Named("post") retrofit: Retrofit
        ): PostApi {
            return retrofit.create(PostApi::class.java)
        }

    }

}