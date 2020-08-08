package com.zavsmit.jokes.data.network.models

data class JokesResponse(
        val type: String,
        val value: List<JokeItemResponse>
)

data class JokeItemResponse(
        val id: Long = 0,
        val joke: String
)