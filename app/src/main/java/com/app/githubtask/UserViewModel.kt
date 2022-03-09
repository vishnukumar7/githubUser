package com.app.githubtask

import android.util.Log
import androidx.lifecycle.*
import com.app.githubtask.model.DataItem
import com.app.githubtask.model.GitHubUser
import com.app.githubtask.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val appRepository: AppRepository) : ViewModel() {

    private val TAG = "UserViewModel"
    val userList: LiveData<MutableList<User>> = appRepository.userList.asLiveData()
    val gitHubUser: MutableLiveData<GitHubUser> = MutableLiveData()

    fun insert(user: User) = viewModelScope.launch {
        appRepository.insert(user)
    }

    fun update(user: User) = viewModelScope.launch {
        appRepository.update(user)
    }

    fun insert(user: List<User>) = viewModelScope.launch {
        appRepository.insert(user)
    }

    fun getGithubUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = appRepository.getGithubUser()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val userList = ArrayList<User>()
                        for (item in it.data) {
                            userList.add(
                                User(
                                    gender = item.gender,
                                    name = item.name,
                                    id = item.id,
                                    email = item.email,
                                    status = item.status,
                                    comment = ""
                                )
                            )
                        }
                        insert(userList)
                    }
                    Log.d(TAG, "getGithubUserData: success ")
                } else {
                    Log.d(TAG, "github user: error " + response.errorBody().toString())
                }
            }
        }
    }


    class UserViewModelFactory(private val appRepository: AppRepository) :
        ViewModelProvider.Factory {

        /**
         * Creates a new instance of the given `Class`.
         *
         * @param modelClass a `Class` whose instance is requested
         * @return a newly created ViewModel
         */
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }
    }
}