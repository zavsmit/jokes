package com.zavsmit.jokes.data.network.models

data class JokesResponseEntity(
        val type : String,
        val value : List<JokesResponseValueEntity>
)

data class JokesResponseValueEntity(
        val id: Long = 0,
        val joke: String
)