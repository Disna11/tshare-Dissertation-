package com.example.tshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnlogin: Button? = null
        var btnreg: Button? = null

        btnlogin= findViewById(R.id.log_button)
        btnreg= findViewById(R.id.reg_button)

        btnlogin.setOnClickListener {
            var intent = Intent(this, loginActivity::class.java)
            startActivity(intent);
        }
        btnreg.setOnClickListener {
            var intent = Intent(this, signupActivity::class.java)
            startActivity(intent);
        }
    }
}