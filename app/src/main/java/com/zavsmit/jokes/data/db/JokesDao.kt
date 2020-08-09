package com.zavsmit.jokes.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zavsmit.jokes.domain.JokesRepository.Companion.ITEM_PER_PAGE
import com.zavsmit.jokes.data.db.models.JokeDb
import com.zavsmit.jokes.data.db.models.MyJokeDb
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface JokesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertJokes(jokes: List<JokeDb>): Completable

    @Query("DELETE FROM joke_db")
    fun deleteJokes(): Completable

    @Query("SELECT * FROM joke_db ORDER BY RANDOM() LIMIT 1")
    fun getRandomJoke(): Observable<List<JokeDb>>

    @Query("SELECT * FROM joke_db ORDER BY id LIMIT :limit OFFSET :offset")
    fun getJokes(offset: Long = 0, limit: Long = ITEM_PER_PAGE): Observable<List<JokeDb>>

    @Query("SELECT * FROM joke_db WHERE id = :id")
    fun getJokeById(id: Long): JokeDb


    @Query("SELECT * FROM my_joke_db ORDER BY id")
    fun getMyJokes(): Observable<List<MyJokeDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMyJoke(myJoke: MyJokeDb)

    @Query("DELETE FROM my_joke_db WHERE id = :id")
    fun deleteMyJoke(id: Long): Completable

    @Query("SELECT id FROM my_joke_db  WHERE id IN (:ids) ORDER BY id")
    fun getMyJokesIdsByIds(ids: List<Long>): List<Long>
}