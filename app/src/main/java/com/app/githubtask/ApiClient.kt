package com.app.githubtask

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private fun getClient(): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://gorest.co.in/")
            .build()
    }

    fun getAPI(): ApiInterface {
        return getClient().create(ApiInterface::class.java)
    }
}