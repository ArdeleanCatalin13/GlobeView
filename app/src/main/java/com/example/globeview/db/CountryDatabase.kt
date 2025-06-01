package com.example.globeview.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.globeview.models.Data

@Database(
    entities = [Data::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CountryDatabase: RoomDatabase() {

    abstract fun getCountryDao(): CountryDAO

    companion object {
        @Volatile
        private var instance: CountryDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CountryDatabase::class.java,
                "countries_db.db"
            ).build()
    }
}