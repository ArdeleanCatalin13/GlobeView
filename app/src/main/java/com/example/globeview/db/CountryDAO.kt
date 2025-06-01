package com.example.globeview.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.globeview.models.Data

@Dao
interface CountryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(data: Data): Long

    @Query("SELECT * FROM countries")
    fun getAllCountries(): LiveData<List<Data>>

    @Delete
    suspend fun deleteCountry(data: Data)
}