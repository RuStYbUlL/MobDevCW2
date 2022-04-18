package com.example.mobdevcw22

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    // Insert data
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie(movie: Movie)

    // Reading data
    @Query("SELECT * FROM Movie ORDER BY id ASC")  //Select all data in ascending order
    suspend fun readAllData(): List<Movie>

    @Query("DELETE from Movie")
    suspend fun deleteAll()
}