package com.zavsmit.jokes.di

import android.app.Application
import com.zavsmit.jokes.data.SharedPrefsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application) = SharedPrefsHelper(application)
}


