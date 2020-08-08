package com.zavsmit.jokes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zavsmit.jokes.data.db.models.JokeDb
import com.zavsmit.jokes.data.db.models.MyJokeIdDb

@Database(entities = [JokeDb::class, MyJokeIdDb::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jokesDao(): JokesDao
}