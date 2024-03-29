package com.example.tshare.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.tshare.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        var btnlogin: Button? = null
        var btnreg: Button? = null

        btnlogin= findViewById(R.id.log_button)
        btnreg= findViewById(R.id.reg_button)

        btnlogin.setOnClickListener {
            var intent = Intent(this, loginActivity::class.java)
            startActivity(intent);
            finish()
        }
        btnreg.setOnClickListener {
            var intent = Intent(this, signupActivity::class.java)
            startActivity(intent);
            finish()
        }
    }
}