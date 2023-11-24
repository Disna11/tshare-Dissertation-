package com.example.tshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class homeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var BottomNavigationView: BottomNavigationView
//    var txt: TextView?=null
//    var btn: Button?=null
    var user: FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth

        BottomNavigationView=findViewById(R.id.btnav)
//        txt=findViewById(R.id.txt)
//        btn=findViewById(R.id.logout)
        user=auth.currentUser
        if(user==null){
            val myIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(myIntent)
            finish()
        }

        BottomNavigationView.setOnItemSelectedListener { menuItem->
            when (menuItem.itemId){
                R.id.btm_home->{
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.btm_search->{
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.btm_add->{
                    replaceFragment(AddFragment())
                    true
                }
                R.id.btm_chat->{
                    replaceFragment(ChatFragment())
                    true
                }
                R.id.btm_profile->{
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false


            }

        }
//        btn?.setOnClickListener {
//            Firebase.auth.signOut()
//            val myIntent = Intent(applicationContext, loginActivity::class.java)
//            startActivity(myIntent)
//            finish()
//        }
        replaceFragment(HomeFragment())
    }

    private  fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frameLyt, fragment).commit()
    }
}