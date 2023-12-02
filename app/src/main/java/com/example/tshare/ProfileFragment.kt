package com.example.tshare

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


class ProfileFragment : Fragment() {

    private lateinit var profilePhotoImageView: ShapeableImageView
    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView
    private lateinit var editPhotoButton: FloatingActionButton
    private lateinit var logoutButton: FloatingActionButton
    var preferenceHelper: preferenceHelper? =null
    var email:String?=null
    var uid:String?=null
    val defaultValue = ""
    var user:FirebaseUser?=null
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize widgets after inflating the layout
        auth = Firebase.auth
        profilePhotoImageView = view.findViewById(R.id.profile_photo)
        profileNameTextView = view.findViewById(R.id.profile_name)
        profileEmailTextView = view.findViewById(R.id.profile_email)
        editPhotoButton = view.findViewById(R.id.profile_edit_photo)
        logoutButton = view.findViewById(R.id.profile_logout)
        preferenceHelper = preferenceHelper(requireContext())
        email= preferenceHelper!!.getString("logedin_email", defaultValue)
        uid= preferenceHelper!!.getString("logedin_dis", defaultValue)
        user=auth.currentUser

        if(user==null){
            val myIntent = Intent(requireActivity(), loginActivity::class.java)
            startActivity(myIntent)
            requireActivity().finish()
        }

        profileEmailTextView.setText(email)
        profileNameTextView.setText(uid)

        editPhotoButton?.setOnClickListener {

        }
        logoutButton?.setOnClickListener {
            preferenceHelper!!.clearSharedPreferences()
            Firebase.auth.signOut()
            val myIntent = Intent(requireActivity(), loginActivity::class.java)
            startActivity(myIntent)
            requireActivity().finish()

        }




        return view
    }
}