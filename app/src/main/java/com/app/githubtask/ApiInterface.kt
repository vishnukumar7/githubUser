package com.app.githubtask

import com.app.githubtask.model.GitHubUser
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("public/v1/users")
    suspend fun githubRepos() : Response<GitHubUser>
}