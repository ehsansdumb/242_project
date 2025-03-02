package com.example.myauth4

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://authdb5-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("MyUsers")

        // UI Elements
        val tvUserEmail = findViewById<TextView>(R.id.tvUserEmail)
        val etName = findViewById<EditText>(R.id.etName)
        val etCity = findViewById<EditText>(R.id.etCity)
        val etCountry = findViewById<EditText>(R.id.etCountry)
        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userEmail = currentUser.email

            tvUserEmail.text = userEmail // Display the logged-in email

            database.child(userId).get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").value.toString()
                    val city = dataSnapshot.child("city").value.toString()
                    val country = dataSnapshot.child("country").value.toString()

                    etName.setText(name)
                    etCity.setText(city)
                    etCountry.setText(country)
                }
            }.addOnFailureListener {
                Log.e("FirebaseError", "Failed to fetch user data")
            }

            btnUpdate.setOnClickListener {
                val newName = etName.text.toString().trim()
                val newCity = etCity.text.toString().trim()
                val newCountry = etCountry.text.toString().trim()
                val newPassword = etNewPassword.text.toString().trim()

                if (newName.isNotEmpty() && newCity.isNotEmpty() && newCountry.isNotEmpty()) {
                    val updatedData = mapOf(
                        "name" to newName,
                        "city" to newCity,
                        "country" to newCountry
                    )

                    database.child(userId).updateChildren(updatedData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show()
                        }
                }

                if (newPassword.isNotEmpty() && newPassword.length >= 6) {
                    currentUser.updatePassword(newPassword)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Password updated!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to update password.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}

// DatabaseMode class
data class DatabaseModel(
    val name: String? = null,
    val city: String? = null,
    val country: String? = null,
    val email: String? = null,
    val password: String? = null
) {
    // No-argument constructor required by Firebase
    constructor() : this("", "", "", "", "")
}
