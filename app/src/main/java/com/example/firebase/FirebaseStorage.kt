package com.example.firebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityFirebaseStorageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseStorage : AppCompatActivity() {

    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityFirebaseStorageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        storageReference = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.selectBtn.setOnClickListener {
            selectImage()
        }

        binding.uploadBtn.setOnClickListener {
            uploadImage()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }
    private fun uploadImage() {
        if (selectedImageUri != null) {
            val imageRef = storageReference.child("images/${auth.currentUser?.uid}/${System.currentTimeMillis()}.jpg")


            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    Toast.makeText(this, "Upload successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                }
        } else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    private var selectedImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            binding.imageView.setImageURI(selectedImageUri)
        }
    }
}