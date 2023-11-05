package com.pena.ismael.socialmediapager.feature.postpager.di

import android.app.DownloadManager
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.pena.ismael.socialmediapager.core.networking.LogHttpInterceptor
import com.pena.ismael.socialmediapager.core.services.connectivityobserver.ConnectivityObserver
import com.pena.ismael.socialmediapager.core.services.connectivityobserver.NetworkConnectivityObserver
import com.pena.ismael.socialmediapager.core.services.downloadmanager.Downloader
import com.pena.ismael.socialmediapager.core.services.downloadmanager.ImageDownloader
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.PostDatabase
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.PostApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PostModule {

    @Binds
    abstract fun bindsDownloader(
        impl: ImageDownloader
    ): Downloader

    @Binds
    abstract fun bindsConnectivityObserver(
        impl: NetworkConnectivityObserver
    ): ConnectivityObserver

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
        fun providePostRetrofit(
            okHttpClient: OkHttpClient
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(PostApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        @Provides
        @Singleton
        fun providePostApi(
            @Named("post") retrofit: Retrofit
        ): PostApi {
            return retrofit.create(PostApi::class.java)
        }

        @Provides
        @Singleton
        fun providesOkHttpClient(
            interceptor: LogHttpInterceptor
        ): OkHttpClient {
            return OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .build()
        }

        @Provides
        @Singleton
        fun provideConnectivityManager(
            @ApplicationContext context: Context,
        ): ConnectivityManager? {
            return context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        }

        @Provides
        @Singleton
        fun provideDownloadManager(
            @ApplicationContext context: Context,
        ): DownloadManager? {
            return context.getSystemService(DownloadManager::class.java)
        }

    }

}

