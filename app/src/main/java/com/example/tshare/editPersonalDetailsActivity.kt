package com.example.tshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class editPersonalDetailsActivity : AppCompatActivity() {
    private lateinit var close: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_personal_details)
        close= findViewById(R.id.close)
        close?.setOnClickListener {
            finish()
        }


    }
}