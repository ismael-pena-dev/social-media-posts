package com.pena.ismael.socialmediapager

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.PostApi
import com.pena.ismael.socialmediapager.feature.postpager.postlist.PostDatabase
import com.pena.ismael.socialmediapager.feature.postpager.postlist.PostListScreen
import com.pena.ismael.socialmediapager.feature.postpager.postlist.PostRemoteMediator
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.entity.PostEntity
import com.pena.ismael.socialmediapager.ui.theme.SocialMediaPagerTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

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
    NavHost(
        navController = navController,
        startDestination = "posts",
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = "posts") {
            PostListScreen()
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PostModule {

    companion object {
        @Provides
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
        @Named("post")
        fun providePostRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(PostApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun providePostApi(
            @Named("post") retrofit: Retrofit
        ): PostApi {
            return retrofit.create(PostApi::class.java)
        }

        @OptIn(ExperimentalPagingApi::class)
        @Provides
        fun providesTextPostPager(
            textPostRemoteMediator: PostRemoteMediator,
            pagingDatabase: PostDatabase,
        ): Pager<Int, PostEntity> {
            return Pager(
                config = PagingConfig(
                    pageSize = 5,
//                    initialLoadSize = 5,
                    prefetchDistance = 5,
                    enablePlaceholders = false
                ),
                remoteMediator = textPostRemoteMediator,
                pagingSourceFactory = {
                    pagingDatabase.postDao().pagingSource()
                },
            )
        }
    }

}