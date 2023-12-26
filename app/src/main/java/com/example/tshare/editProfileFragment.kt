package com.example.tshare

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class editProfileFragment : Fragment() {
    private lateinit var editMail: TextView
    private lateinit var firstName: TextView
    private lateinit var editLastname:TextView
    private lateinit var editAddress:TextView
    private lateinit var editDob:TextView
    private lateinit var editPhone:TextView
    private lateinit var lastnameEditButton: ImageButton
    private lateinit var addressEditButton: ImageButton
    private lateinit var dobEditButton: ImageButton
    private lateinit var phoneEditButton: ImageButton
     var vemail:String?=null
    var currentuid:String?=null
    var vfirstName:String?=null
    val defaultValue = ""
    var preferenceHelper: preferenceHelper? =null
    private lateinit var auth: FirebaseAuth
    var user: FirebaseUser?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val closeButton = view.findViewById<FloatingActionButton>(R.id.close_button)
        editMail=view.findViewById(R.id.editEmail)
        firstName=view.findViewById(R.id.editfirstName)
        editLastname=view.findViewById(R.id.editlastName)
        editAddress=view.findViewById(R.id.editAddress)
        editDob=view.findViewById(R.id.editDob)
        editPhone=view.findViewById(R.id.editphone)
        lastnameEditButton=view.findViewById(R.id.editlastname_button)
        addressEditButton=view.findViewById(R.id.editaddress_button)
        dobEditButton=view.findViewById(R.id.editdob_button)
        phoneEditButton=view.findViewById(R.id.editphone_button)
        auth = Firebase.auth
        preferenceHelper = preferenceHelper(requireContext())
        vemail= preferenceHelper!!.getString("logedin_email", defaultValue)
        vfirstName= preferenceHelper!!.getString("firstname", defaultValue)
        currentuid= preferenceHelper!!.getString("userid", defaultValue)
        user=auth.currentUser

        closeButton?.setOnClickListener {
            // Navigate back to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }
        editMail.setText(vemail)
        firstName.setText(vfirstName)

        lastnameEditButton?.setOnClickListener {
            val editLastnameFragment = lastNameEditFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, editLastnameFragment)
            transaction.addToBackStack(null)  // Optional: Add to back stack
            transaction.commit()
        }
        addressEditButton?.setOnClickListener {
            val editAddressFragment = addressEditFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, editAddressFragment)
            transaction.addToBackStack(null)  // Optional: Add to back stack
            transaction.commit()
        }
        dobEditButton?.setOnClickListener {
            val editDobFragment = dobEditFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, editDobFragment)
            transaction.addToBackStack(null)  // Optional: Add to back stack
            transaction.commit()
        }
        phoneEditButton?.setOnClickListener {
            val editPhoneFragment = phoneEditFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, editPhoneFragment)
            transaction.addToBackStack(null)  // Optional: Add to back stack
            transaction.commit()
        }



        if(currentuid!=null){
            val database = FirebaseDatabase.getInstance()
            val usersPath = "users/$currentuid"
            val currentUserRef = database.getReference(usersPath)
            currentUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
                        // Retrieve the username for the current user
                        val address = dataSnapshot.child("address").getValue(String::class.java)
                        val lastname= dataSnapshot.child("lastname").getValue(String::class.java)
                        val dob= dataSnapshot.child("dob").getValue(String::class.java)
                        val phone= dataSnapshot.child("phone").getValue(String::class.java)
                        if(!address.isNullOrEmpty()  ){
                            editAddress.setText(address)
                        }else {
                            editAddress.setText(("Add Address"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                        if(!lastname.isNullOrEmpty()){
                            editLastname.setText(lastname)
                        }else{
                            editLastname.setText(("Add Last name"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                        if(!dob.isNullOrEmpty()){
                            editDob.setText(dob)
                        }else{
                            editDob.setText(("Add Date of Birth"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                        if(!phone.isNullOrEmpty()){
                            editPhone.setText(phone)
                        }else{
                            editPhone.setText(("Add phone number"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                    }else{
                        editPhone.setText(("Add phone number"))
                        editDob.setText(("Add Date of Birth"))
                        editLastname.setText(("Add Last name"))
                        editAddress.setText(("Add Address"))
                        Log.e("FirebaseData", "Data does not exist for the current user")

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FirebaseData", "Error reading data from Firebase: ${databaseError.message}")

                }
            })
        }



        return view
    }



}