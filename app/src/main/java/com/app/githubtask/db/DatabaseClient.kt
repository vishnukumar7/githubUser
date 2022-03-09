package com.app.githubtask.db

import android.content.Context
import androidx.room.Room

object DatabaseClient {

    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room
            .databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "users"
            )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()


}