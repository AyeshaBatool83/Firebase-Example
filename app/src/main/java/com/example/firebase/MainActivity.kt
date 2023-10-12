package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var name: String
    private lateinit var type: String
    private lateinit var email: String
    private var current = 0
    private var dataList = ArrayList<UserModel>()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.insertbtn.setOnClickListener {
            name = binding.nameEt.text.toString()
            type = binding.typeEt.text.toString()
            email = binding.emailEt.text.toString()
            /* if (name.isEmpty() || type.isEmpty()) {
                 Toast.makeText(this, "All field is Required!!", Toast.LENGTH_SHORT).show()
                 return@setOnClickListener
             }
             createDB()*/
//            if (name.isEmpty() || type.isEmpty() || email.isEmpty()) {
//                Toast.makeText(this, "All field is Required!!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            registerUser()
            signInUser()
        }
        binding.readbtn.setOnClickListener {
            readKeyFromDB()
        }
        binding.prevBtn.setOnClickListener {
            if (current > 0) {
                current--
                updateUI()
            } else if (current == 0) {
                current = dataList.size - 1
                updateUI()
            }
        }

        binding.nextBtn.setOnClickListener {
            if (current < dataList.size - 1) {
                current++
                updateUI()
            } else if (current == dataList.size - 1) {
                current = 0
                updateUI()
            }
        }

        binding.updatebtn.setOnClickListener {
            name = binding.nameEt.text.toString()
            type = binding.typeEt.text.toString()
            val uid = dataList[current].uid
            val data = HashMap<String, Any>()
            data["name"] = name
            data["type"] = type
            updateDB(uid, data)
        }
        binding.deletebtn.setOnClickListener {
            val uid = dataList[current].uid
            deleteDataThroughKeyFromDB(uid)
        }
    }

    private fun signInUser(){
        auth.signInWithEmailAndPassword(email,type)
            .addOnSuccessListener {
                startActivity(Intent(this@MainActivity,FirebaseStorage::class.java))
            }
            .addOnSuccessListener {
                Log.d("AYESHA", "signIn: $it")
                Toast.makeText(this@MainActivity,"Error!! $it",Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerUser() {
        auth.createUserWithEmailAndPassword(email, type)
            .addOnSuccessListener {
                Toast.makeText(this@MainActivity,"Register Successfully",Toast.LENGTH_SHORT).show()
                createDB()
            }
            .addOnFailureListener {
                Log.d("AYESHA", "registerUser: $it")
                Toast.makeText(this@MainActivity,"Error!! $it",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(this@MainActivity,FirebaseStorage::class.java))
        }
        else {
            Toast.makeText(this@MainActivity,"Sign In Please",Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSignedUI(currentUser: FirebaseUser?) {

    }

    private fun signOutUser() {
        Firebase.auth.signOut()
    }

    private fun deleteDataThroughKeyFromDB(key: String) {
        val database = Firebase.database
        val myRef = database.getReference("task")
        myRef.child(key).removeValue()
    }

    private fun readKeyFromDB() {
        val database = Firebase.database
        val myRef = database.getReference("task")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataList.clear() // Clear the list before adding new data
                for (data in snapshot.children) {
                    val name = data.child("name").value as String
                    val type = data.child("type").value as String
                    val uid = data.child("uid").value as String
                    dataList.add(UserModel(name, type, uid))


                }
                updateUI()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }

    private fun updateUI() {
        val selected = dataList[current]
        name = selected.name
        type = selected.type
        binding.nameEt.setText(name)
        binding.typeEt.setText(type)
    }

    private fun updateDB(parent: String, updatedData: HashMap<String, Any>) {
        val database = Firebase.database
        val myRef = database.getReference("task")

        myRef.child(parent).updateChildren(updatedData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Update Successful", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@MainActivity, "Error!! ${it.exception}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }

    private fun createDB() {
        val database = Firebase.database
        val myRef = database.getReference("task")

        val dataMap = HashMap<String, String>()
        dataMap["name"] = name
        dataMap["type"] = type
        dataMap["email"] = email
        dataMap["uid"] = auth.currentUser!!.uid

        myRef.child(auth.currentUser!!.uid).setValue(dataMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Insert Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error,Task not Insert", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}
