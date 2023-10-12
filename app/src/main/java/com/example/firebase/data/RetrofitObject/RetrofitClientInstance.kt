package com.example.firebase.data.RetrofitObject

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://jsonplaceholder.typicode.com"

    // Singleton Design Pattern
    fun getRetrofitInstance(): Retrofit? {
        if (retrofit == null) {
            // Factory Design Pattern
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}