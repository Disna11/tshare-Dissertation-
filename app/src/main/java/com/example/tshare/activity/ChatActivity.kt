package com.example.tshare.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tshare.R
import com.example.tshare.adapter.ChatAdapter
import com.example.tshare.model.Chat
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
    private  var btnSendMessage: ImageButton?= null
    private var etMessage: EditText?=null
    var chatList = ArrayList<Chat>()
    private var chatRecyclerView:RecyclerView?=null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var intent= getIntent()
        var userId= intent.getStringExtra("UserId")

        imgProfile= findViewById(R.id.imgProfile)
        tvUserName=findViewById(R.id.tvUserName)
        imgBack=findViewById(R.id.imgBack)
        btnSendMessage=findViewById(R.id.btnSendMessage)
        etMessage= findViewById(R.id.etMessage)
        chatRecyclerView=findViewById(R.id.chatRecyclerView)

        chatRecyclerView?.layoutManager = LinearLayoutManager(this)

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

            }

        })

        btnSendMessage?.setOnClickListener {
            val message= etMessage?.text.toString()
            if(message.isEmpty()){
                Toast.makeText(applicationContext,"Type your message",Toast.LENGTH_SHORT).show()
                etMessage?.setText("")
            }else{
                sendMessage(firebaseUser!!.uid,userId!!,message)
                etMessage?.setText("")
            }
        }

        readMessage(firebaseUser!!.uid,userId!!)
    }

    private fun sendMessage(senderId:String, recieverId:String,message: String){
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()


        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId",senderId)
        hashMap.put("recieverId",recieverId)
        hashMap.put("message",message)

        reference!!.child("Chat").push().setValue(hashMap)
    }
    fun  readMessage(senderId: String, receiverId: String){
        val databaseReference: DatabaseReference=
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children){
                    val chat= dataSnapShot.getValue(Chat::class.java)

                    if(chat!!.senderId.equals(senderId) && chat!!.recieverId.equals(receiverId) || chat!!.senderId.equals(receiverId) && chat!!.recieverId.equals(senderId) ){
                        chatList.add(chat)
                    }
                }

                val chatAdapter= ChatAdapter(this@ChatActivity,chatList)
                chatRecyclerView?.adapter=chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}