package com.zavsmit.jokes.data.network

import com.zavsmit.jokes.data.network.models.JokesResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface JokesApi {
    @GET("jokes")
    fun getJokesList(): Observable<JokesResponse>

    @GET("jokes")
    fun getJokesListWithCustomName(
            @Query("firstName") firstName: String = "",
            @Query("lastName") lastName: String = ""
    ): Observable<JokesResponse>
}