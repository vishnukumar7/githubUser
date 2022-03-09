package com.app.githubtask.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Insert
    fun insertList(userList: List<User>)

    @Query("select * from users")
    fun dataList(): Flow<MutableList<User>>

    @Query("select * from users where id=:id")
    fun getListById(id: Int): List<User>

    @Query("select * from users where id=:id limit 1")
    fun getItemById(id: Int): User
}