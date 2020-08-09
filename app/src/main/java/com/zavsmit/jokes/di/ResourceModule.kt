package com.zavsmit.jokes.di

import android.app.Application
import com.zavsmit.jokes.data.ResourceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
class ResourceModule {

    @Provides
    fun provideResourceManager(application: Application) = ResourceManager(application)
}