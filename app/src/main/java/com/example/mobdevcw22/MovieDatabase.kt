package com.example.mobdevcw22

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [Movie :: class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase(){

    abstract fun movieDao(): MovieDao

    companion object{
        @Volatile // no matter what thread tries to access the instance it will always get the current instance, not one that is cached/out of date
        private var INSTANCE: MovieDatabase? = null // make MovieDatabase a singleton class - one instance of the class can be created and used everywhere.

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): MovieDatabase{
            val tempInstance = INSTANCE

            // If tempInstance already exists
            if(tempInstance != null){
                return tempInstance
            }
            // If instance is null
            // Creating an instance of ROOM database
            // This block will be protected by concurrent execution by multiple threads
            // Whatever thread enters the synchronised block, it is locked and makes sure no other threads can access at the same time
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                ).build()
                INSTANCE = instance
                Log.d("TAG", "Getting the database instance")
                return instance
            }
        }
    }
}