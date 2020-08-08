package com.zavsmit.jokes.di

import android.app.Application
import androidx.room.Room
import com.zavsmit.jokes.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class DbModule {
    @Provides
    @Singleton
    fun provideAppDB(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "appDb")
//            .allowMainThreadQueries()
                .build()
    }

    @Provides
    @Singleton
    fun provideJokesDao(appDb: AppDatabase) = appDb.jokesDao()
}