package com.app.githubtask

import androidx.annotation.WorkerThread
import com.app.githubtask.model.DataItem
import com.app.githubtask.model.User
import com.app.githubtask.model.UserDao
import kotlinx.coroutines.flow.Flow

class AppRepository(private val userDao: UserDao,private var apiInterface: ApiInterface) {

    val userList: Flow<MutableList<User>> = userDao.dataList()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: List<User>){
        userDao.insertList(user)
    }

    suspend fun getGithubUser() =apiInterface.githubRepos()


}