package com.zavsmit.jokes.di

import android.app.Application
import com.zavsmit.jokes.core.ConnectivityState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
class UtilsModule {

    @Provides
    fun provideConnectivityState(application: Application) = ConnectivityState(application)
}