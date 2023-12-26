package com.example.tshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class signupActivity : AppCompatActivity() {

//    declare all the widgets here

    var signup_email: EditText?=null
    var signup_password: EditText?=null
    var signup_name: EditText?=null
    var signup_confirm_password: EditText?=null
    var signup_button: Button?=null
    var tologin_text: TextView?=null
   // var profilePicture:String?=" "

    // declare progressbar

    var prog: ProgressBar?=null

    //declare the firebase instance

    private lateinit var auth: FirebaseAuth

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
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        //initialize all the widgets here

        signup_email = findViewById(R.id.email)
        signup_password= findViewById(R.id.password)
        signup_name= findViewById(R.id.name)
        signup_confirm_password= findViewById(R.id.cpassword)
        signup_button= findViewById(R.id.register)
        tologin_text= findViewById(R.id.login_text)

        //initialize progressbar

        prog=findViewById(R.id.reg_progress)

        //initialize firebase auth

        auth= Firebase.auth

        // when "alredy have an account?? Login!!" is clicked, add an intent to go to loginActivity.

        tologin_text?.setOnClickListener {
            val myIntent = Intent(applicationContext,loginActivity::class.java)
            startActivity(myIntent)
            finish()

        }

        signup_button?.setOnClickListener {

            prog?.visibility=View.VISIBLE
            val email=signup_email?.text.toString()
            val password=signup_password?.text.toString()
            val c_password=signup_confirm_password?.text.toString()
            val name=signup_name?.text.toString()
            val database = Firebase.database

            if(email.isNullOrBlank()){
                Toast.makeText(this, "enter a valid username", Toast.LENGTH_SHORT).show()
                prog?.visibility= View.GONE
            }else if(password.isNullOrBlank()){
                Toast.makeText(this, "ente a valid passord", Toast.LENGTH_SHORT).show()
                prog?.visibility= View.GONE
            }else if(name.isNullOrBlank()){
                Toast.makeText(this, "ente your name", Toast.LENGTH_SHORT).show()
                prog?.visibility= View.GONE
            }else if(c_password.isNullOrBlank()){
                Toast.makeText(this,"Re-enter the password", Toast.LENGTH_SHORT).show()
                prog?.visibility=View.GONE
            }else if(password != c_password){
                Toast.makeText(this,"Password mis-match", Toast.LENGTH_SHORT).show()
                prog?.visibility=View.GONE
            } else{

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        prog?.visibility=View.GONE
                        if (task.isSuccessful) {

                            // Get the UID of the newly created user
                            val uid = auth.currentUser?.uid
                            if (uid != null) {
                                // Create a reference to the user node using the UID
                                val userReference = database.getReference("users/$uid")
                                // Create a User object with the provided information
                                val newUser = User(name, email, "","","","","")
                                userReference.setValue(newUser)
                                    .addOnCompleteListener { databaseTask ->
                                        if (databaseTask.isSuccessful) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(
                                                this,
                                                "Account successfully created",
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                            val myIntent = Intent(
                                                applicationContext,
                                                loginActivity::class.java
                                            )
                                            startActivity(myIntent)
                                            finish()
                                        } else {
                                            // Handle the case where database update fails
                                            Toast.makeText(
                                                this,
                                                "Failed to update user information in the database",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }


                            } else {
                                // Handle the case where UID is null
                                Toast.makeText(
                                    this,
                                    "Failed to get user ID",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            val errorMessage = task.exception?.localizedMessage ?: "Authentication failed."
                            Toast.makeText(
                                baseContext,
                                errorMessage,
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            }
        }

    }
}