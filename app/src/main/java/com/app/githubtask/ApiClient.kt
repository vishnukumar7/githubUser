package com.app.githubtask

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var retrofit: Retrofit? = null

    private fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://gorest.co.in/")
                .build()
        }
        return retrofit
    }

    fun getAPI(): ApiInterface {
        return getClient()!!.create(ApiInterface::class.java)
    }
}