package com.example.tshare

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.util.UUID
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {

    private lateinit var profilePhotoImageView: ShapeableImageView
    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView
    private lateinit var editPhotoButton: FloatingActionButton
    private lateinit var logoutButton: FloatingActionButton
    private  lateinit var  editprofiletxt: TextView
    private  lateinit var  updatePasswordtxt: TextView
    private  lateinit var  vehicleinfo: TextView
    var preferenceHelper: preferenceHelper? =null
    var email:String?=null
    var currentuid:String?=null
    val defaultValue = ""
    var user:FirebaseUser?=null
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri?=null
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
        editprofiletxt=view.findViewById(R.id.editprofile)
        updatePasswordtxt=view.findViewById(R.id.resetPassword)
        vehicleinfo=view.findViewById(R.id.vehicleInformation)
        email= preferenceHelper!!.getString("logedin_email", defaultValue)
        currentuid= preferenceHelper!!.getString("userid", defaultValue)
        user=auth.currentUser

        if(user==null){
            val myIntent = Intent(requireActivity(), loginActivity::class.java)
            startActivity(myIntent)
            requireActivity().finish()
        }else{
            loadProfilePicture()
        }

        profileEmailTextView.setText(email)
        if(currentuid!=null){
            val database = FirebaseDatabase.getInstance()
            val usersPath = "users/$currentuid"
            val currentUserRef = database.getReference(usersPath)
            currentUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
                        // Retrieve the username for the current user
                        val username = dataSnapshot.child("username").getValue(String::class.java)
                        profileNameTextView.setText(username)
                        if (username != null) {
                            preferenceHelper?.saveString("firstname", username)
                        }

                    }else{
                        Log.e("FirebaseData", "Data does not exist for the current user")

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FirebaseData", "Error reading data from Firebase: ${databaseError.message}")

                }
            })
        }
//

        editPhotoButton?.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.type = "image/*"
            startActivityForResult(photoIntent, 1)
        }

      editprofiletxt?.setOnClickListener {
          val editProfileFragment = editProfileFragment()
          val transaction = requireActivity().supportFragmentManager.beginTransaction()
          transaction.replace(R.id.fragment_container, editProfileFragment)
          transaction.addToBackStack(null)  // Optional: Add to back stack
          transaction.commit()
        }
        updatePasswordtxt?.setOnClickListener {
            val changePasswordFragment= changePasswordFragment()
            val transaction= requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,changePasswordFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        vehicleinfo?.setOnClickListener {
            val addVehicleInformation=addVehicleInformation()
            val transaction=requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,addVehicleInformation)
            transaction.addToBackStack(null)
            transaction.commit()
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

    private fun loadProfilePicture() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val profilePictureRef = FirebaseDatabase.getInstance().getReference("users/$uid/profilePicture")

        profilePictureRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val profilePictureUrl = dataSnapshot.value.toString()

                // Use Glide to load the image into the ImageView
                Glide.with(requireContext())
                    .load(profilePictureUrl)
//                    .placeholder(R.drawable.profile_photo) // Placeholder image
//                    .error(R.drawable.default_profile_image)       // Error image if loading fails
                    .into(profilePhotoImageView)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.e("ProfileFragment", "Error loading profile picture", databaseError.toException())
            }
        })
    }

    // Override onActivityResult method to handle the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Handle the selected image URI here
             selectedImageUri = data.data
            getImageInImageView()

        }
    }

    private fun getImageInImageView() {
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
        profilePhotoImageView.setImageBitmap(bitmap)
        uploadImage()
    }

    private fun uploadImage() {
        if (selectedImageUri != null) {
            val storageReference = FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString())
            val uploadTask = storageReference.putFile(selectedImageUri!!)

            uploadTask.addOnCompleteListener { taskSnapshot ->
                if (taskSnapshot.isSuccessful) {
                    taskSnapshot.getResult()?.storage?.downloadUrl?.addOnCompleteListener { urlTask ->
                        if (urlTask.isSuccessful) {
                            updateProfilePicture(urlTask.result.toString())
                        }
                    }

                    Toast.makeText(requireContext(), "Profile picture updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to update profile picture", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }




    private fun updateProfilePicture(url: String) {
        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().currentUser?.uid + "/profilePicture").setValue(url).
            addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Database update successful
                    Log.d("ProfileFragment", "Database update successful")
                } else {
                    // Database update failed
                    Log.e("ProfileFragment", "Failed to update database", task.exception)
                }
            }
    }

}