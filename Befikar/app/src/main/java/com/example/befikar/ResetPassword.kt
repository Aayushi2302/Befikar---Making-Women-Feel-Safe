package com.example.befikar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var resetButton: Button
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        email = findViewById(R.id.rinputEmail)
        resetButton = findViewById(R.id.rsendButton)

        auth = FirebaseAuth.getInstance()

        resetButton.setOnClickListener {
            val rEmail = email.text.toString().trim()

            auth.sendPasswordResetEmail(rEmail)
                .addOnSuccessListener {
                    Toast.makeText(this,"Check Your Email",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
                }
        }
    }
}