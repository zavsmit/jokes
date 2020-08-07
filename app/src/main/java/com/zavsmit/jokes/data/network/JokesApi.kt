package com.zavsmit.jokes.data.network

import com.zavsmit.jokes.data.network.models.JokesResponseEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface JokesApi {
    @GET("jokes")
    fun getJokesList(): Single<JokesResponseEntity>

    @GET("jokes")
    fun getJokesListWithCustomName(
            @Query("firstName") firstName: String = "",
            @Query("lastName") lastName: String = ""
    ): Single<JokesResponseEntity>
}