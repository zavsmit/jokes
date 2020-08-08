package com.zavsmit.jokes.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zavsmit.jokes.data.db.models.JokeDb
import com.zavsmit.jokes.data.db.models.MyJokeIdDb
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface JokesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertJokes(jokes: List<JokeDb>): Completable

    @Query("DELETE FROM joke_db")
    fun deleteJokes(): Completable

    @Query("SELECT * FROM joke_db ORDER BY RANDOM() LIMIT 1")
    fun getRandomJoke(): Single<JokeDb>

    @Query("SELECT * FROM joke_db ORDER BY id LIMIT :limit OFFSET :offset")
    fun getJokes(offset: Int, limit: Long): Observable<List<JokeDb>>


    @Query("SELECT * FROM my_joke_id_db")
    fun getMyJokes(): Single<List<MyJokeIdDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMyJoke(myJoke: MyJokeIdDb): Completable

    @Query("DELETE FROM my_joke_id_db WHERE id = :id")
    fun deleteMyJoke(id: Long): Completable

//    @Query("SELECT * FROM joke_db  WHERE id IN (:ids) ORDER BY id")
//    fun getMyJokesDb(ids: List<String>): Single<List<JokeDb>>


    @Query("SELECT * FROM joke_db, my_joke_id_db  WHERE joke_db.id == my_joke_id_db.id ORDER BY id")
    fun getMyJokesDb(): Single<List<JokeDb>>

}