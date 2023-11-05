package com.pena.ismael.socialmediapager.feature.postpager.di

import android.content.Context
import androidx.room.Room
import com.pena.ismael.socialmediapager.core.services.downloadmanager.ImageDownloader
import com.pena.ismael.socialmediapager.core.services.downloadmanager.Downloader
import com.pena.ismael.socialmediapager.feature.postpager.repository.local.PostDatabase
import com.pena.ismael.socialmediapager.feature.postpager.repository.remote.PostApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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