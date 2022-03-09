package com.app.githubtask.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.githubtask.model.User
import com.app.githubtask.model.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}