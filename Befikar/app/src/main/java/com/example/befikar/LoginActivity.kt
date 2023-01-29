package com.example.befikar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        lnewLogin.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

       loginButton.setOnClickListener {
            val pwd = linputPassword.text.toString()
            val email = linputEmail.text.toString().trim()

            if(email.isNotEmpty() && pwd.isNotEmpty())
            {
                firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener{

                    if(it.isSuccessful){

                        val intent = Intent(this,Home::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Empty Fields  is not allowed", Toast.LENGTH_SHORT).show()
            }
        }

        lresetPassword.setOnClickListener {
            val intent = Intent(this,ResetPassword::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null)
        {
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
        }
    }
}