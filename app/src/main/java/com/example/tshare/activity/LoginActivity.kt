package com.example.tshare.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.tshare.R
import com.example.tshare.preferenceHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference

class loginActivity : AppCompatActivity() {

    // declare progressbar

    var prog: ProgressBar?=null

    //declare the firebase instance

    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference

    //declare all other widgets

    var user: EditText?=null
    var lpassword: EditText?=null
    var logbutton:Button?=null
    var tosignup: TextView?=null
    var preferenceHelper: preferenceHelper? =null
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val myIntent = Intent(applicationContext, homeActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        //initialize progressbar

        prog=findViewById(R.id.log_progress)

        //initialize firebase auth

        auth= Firebase.auth

        //initialise the widgets

        user=findViewById(R.id.loginUsername)
        lpassword=findViewById(R.id.loginPassword)
        logbutton=findViewById(R.id.log_button)
        tosignup=findViewById(R.id.signup_text)
        preferenceHelper = preferenceHelper(applicationContext)


        // when "alredy have an account?? Login!!" is clicked, add an intent to go to loginActivity.

        tosignup?.setOnClickListener {
            val myIntent = Intent(applicationContext, signupActivity::class.java)
            startActivity(myIntent)
            finish()

        }

        //when login button is clicked

        logbutton?.setOnClickListener {
            prog?.visibility= View.VISIBLE
            val email=user?.text.toString()
            val password=lpassword?.text.toString()
            if(email.isNullOrBlank()){
                Toast.makeText(this, "enter a valid username", Toast.LENGTH_SHORT).show()
                prog?.visibility= View.GONE
            }else if(password.isNullOrBlank()){
                Toast.makeText(this,"enter the password",Toast.LENGTH_SHORT).show()
                prog?.visibility=View.GONE
            }else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        prog?.visibility = View.GONE
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(
                                this,
                                "Login Successful",
                                Toast.LENGTH_SHORT,
                            ).show()
                            // Get the current user
                            val currentUser = auth.currentUser

                            // Check if the user is not null
                            if (currentUser != null) {
                                // Access user information
                                val uid = currentUser.uid?.toString() ?: ""
                                val email = currentUser.email?.toString() ?:""


                                preferenceHelper?.saveString("logedin_email", email)
                                preferenceHelper?.saveString("userid", uid)
                                preferenceHelper?.saveString("userpassword", password)
                                var ppp=preferenceHelper?.getString("userpassword","")

                            }
                            val myIntent = Intent(applicationContext, homeActivity::class.java)
                            startActivity(myIntent)
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            val errorMessage = task.exception?.message
                            Toast.makeText(
                                this,
                                errorMessage,
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            }
        }
    }
}