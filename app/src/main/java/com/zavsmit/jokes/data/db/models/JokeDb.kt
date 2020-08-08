package com.zavsmit.jokes.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "joke_db")
data class JokeDb(
        @PrimaryKey
        val id: Long,
        val joke: String
)

@Entity(tableName = "my_joke_id_db")
data class MyJokeIdDb(
        @PrimaryKey
        val id: Long
)