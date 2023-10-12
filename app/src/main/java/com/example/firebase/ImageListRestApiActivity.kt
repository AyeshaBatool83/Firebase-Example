package com.example.firebase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.data.RetrofitObject.RetrofitClientInstance
import com.example.firebase.data.adapter.CustomAdapter
import com.example.firebase.data.interfaces.Api
import com.example.firebase.data.models.RetroPhotoItem
import com.example.firebase.databinding.ActivityImageListRestApiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ImageListRestApiActivity : AppCompatActivity() {
    private lateinit var binding:ActivityImageListRestApiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageListRestApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ref = RetrofitClientInstance.getRetrofitInstance()
        val api = ref?.create(Api::class.java)
        val call = api?.getAllPhotos()


        call?.enqueue(object : Callback<List<RetroPhotoItem?>?> {
            override fun onResponse(
                call: Call<List<RetroPhotoItem?>?>,
                response: Response<List<RetroPhotoItem?>?>
            ) {

                if(response.isSuccessful) {
                    generateDataList(response.body() as List<RetroPhotoItem>)
                }
            }

            override fun onFailure(call: Call<List<RetroPhotoItem?>?>, t: Throwable) {
                Toast.makeText(
                    this@ImageListRestApiActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private fun generateDataList(photoList: List<RetroPhotoItem>) {
        binding.rv.adapter = CustomAdapter(photoList)
    }
}