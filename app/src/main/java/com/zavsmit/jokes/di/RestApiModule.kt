package com.zavsmit.jokes.di

import com.zavsmit.jokes.data.network.JokesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module
@InstallIn(ApplicationComponent::class)
class RestApiModule {

    @Provides
    fun provideJokesApi(retrofit: Retrofit) = retrofit.create(JokesApi::class.java)
}