package com.example.tshare.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.tshare.R
import com.example.tshare.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class ChatActivity : AppCompatActivity() {

    var firebaseUser:FirebaseUser?=null
    var  reference: DatabaseReference?=null
    private  var imgProfile: CircleImageView?= null
    private  var tvUserName: TextView?= null
    private  var imgBack: ImageView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var intent= getIntent()
        var userId= intent.getStringExtra("UserId")

        imgProfile= findViewById(R.id.imgProfile)
        tvUserName=findViewById(R.id.tvUserName)
        imgBack=findViewById(R.id.imgBack)

        imgBack?.setOnClickListener {
            onBackPressed()
        }

        firebaseUser= FirebaseAuth.getInstance().currentUser
        reference= FirebaseDatabase.getInstance().getReference("users/$userId")
        reference!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user= snapshot.getValue(User::class.java)
                tvUserName?.text=user!!.username



                if (user!!.profilePicture==""){
                    imgProfile?.setImageResource(R.drawable.icon_profile)
                }else{
                    Glide.with(this@ChatActivity).load(user.profilePicture).into(imgProfile!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}