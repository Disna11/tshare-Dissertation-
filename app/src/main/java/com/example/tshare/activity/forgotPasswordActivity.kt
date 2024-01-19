package com.example.tshare.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tshare.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class forgotPasswordActivity : AppCompatActivity() {

    var emailId: EditText?=null
    var send: Button?=null
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val closeButton = findViewById<FloatingActionButton>(R.id.close_button)
        closeButton?.setOnClickListener {
            val myIntent = Intent(applicationContext, loginActivity::class.java)
            startActivity(myIntent)
            finish()
        }


        emailId=findViewById(R.id.passwordEmail)
        send=findViewById(R.id.sendButton)
        auth= Firebase.auth


            send?.setOnClickListener {

                if(emailId?.text.toString().isEmpty()) {
                    Toast.makeText(this, "enter a valid email id", Toast.LENGTH_SHORT).show()
                }else{
                    val email=emailId?.text.toString()
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "please check your email address", Toast.LENGTH_SHORT,).show()

                            val myIntent = Intent(applicationContext, loginActivity::class.java)
                            startActivity(myIntent)
                            finish()

                        }else{
                            Toast.makeText(this, "failed to send verification code", Toast.LENGTH_SHORT,).show()

                        }

                    }.addOnFailureListener {
                        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT,).show()

                    }
                }



            }


    }


}