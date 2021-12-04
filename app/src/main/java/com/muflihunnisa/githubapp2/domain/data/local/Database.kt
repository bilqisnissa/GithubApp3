package com.muflihunnisa.githubapp2.domain.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muflihunnisa.githubapp2.domain.data.model.DetailUserResponse

@androidx.room.Database(entities = [DetailUserResponse::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun favoriteDao() : FavoriteDao
    companion object{
        @Volatile
        var instance : Database? = null

        fun database(context: Context) : Database{
            if (instance == null){
                synchronized(Database::class.java){
                    instance = Room.databaseBuilder(
                        context.applicationContext, Database::class.java, "favorite"
                    ).build()
                }
            }
            return instance as Database
        }
    }
}