package com.zavsmit.jokes.di

import android.app.Application
import com.zavsmit.jokes.core.sensor.ShakeEventProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class SensorModule {

    @Provides
    fun provideShakeProvider(application: Application) = ShakeEventProvider(application)
}