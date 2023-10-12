package com.example.firebase.data.interfaces

import com.example.firebase.data.models.RetroPhotoItem
import retrofit2.Call
import retrofit2.http.GET


interface Api {

    @GET("/photos")
    fun getAllPhotos(): Call<List<RetroPhotoItem?>?>?

}