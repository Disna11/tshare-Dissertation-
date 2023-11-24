package com.example.tshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class homeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var txt: TextView?=null
    var btn: Button?=null
    var user: FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth
        txt=findViewById(R.id.txt)
        btn=findViewById(R.id.logout)
        user=auth.currentUser
        if(user==null){
            val myIntent = Intent(applicationContext, loginActivity::class.java)
            startActivity(myIntent)
            finish()
        }
        btn?.setOnClickListener {
            Firebase.auth.signOut()
            val myIntent = Intent(applicationContext, loginActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }
}