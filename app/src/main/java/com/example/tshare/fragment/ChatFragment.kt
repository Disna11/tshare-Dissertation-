package com.example.tshare.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tshare.R
import com.example.tshare.adapter.userAdapter
import com.example.tshare.model.UserWithId
import com.example.tshare.model.chatUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var imgBackBtn: CircleImageView
    private lateinit var dbref: DatabaseReference
    private lateinit var userChatArraylist: ArrayList<chatUser>
    private  var profileName: TextView?= null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        userRecyclerView = view.findViewById(R.id.userRecyclerView)
        imgBackBtn = view.findViewById(R.id.proPic)
        profileName=view.findViewById(R.id.profileName)

        userRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        userRecyclerView.setHasFixedSize(true)
        auth = Firebase.auth

        val currentUser = auth.currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val profilePictureRef = FirebaseDatabase.getInstance().getReference("users/$uid/profilePicture")
            val profileNameRef = FirebaseDatabase.getInstance().getReference("users/$uid/username")

            profilePictureRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val profilePictureUrl = dataSnapshot.value.toString()

                    // Use Glide to load the image into the ImageView
                    Glide.with(requireContext())
                        .load(profilePictureUrl)
                        // Uncomment and customize placeholder and error handling as needed
                        .placeholder(R.drawable.icon_profile)
                        // .error(R.drawable.default_profile_image)
                        .into(imgBackBtn)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.e("ProfileFragment", "Error loading profile picture", databaseError.toException())
                }
            })

            profileNameRef.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val proName= snapshot.value.toString()
                    profileName?.setText(proName)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            getUsers()
        }

        return view
    }

    private fun getUsers() {
        val currentUser = auth.currentUser
        val currentId = currentUser?.uid?.toString() ?: ""

        dbref = FirebaseDatabase.getInstance().getReference("users")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userWithIdList = ArrayList<UserWithId>()

                    for (userSnapshot in snapshot.children) {
                        val chatUser = userSnapshot.getValue(chatUser::class.java)
                        val uid = userSnapshot.key
                        if (uid != currentId) {
                            val userWithId = UserWithId(uid!!, chatUser!!)
                            userWithIdList.add(userWithId)
                        }
                    }

                    userRecyclerView.adapter = userAdapter(requireActivity(), userWithIdList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }
        })
    }
}
