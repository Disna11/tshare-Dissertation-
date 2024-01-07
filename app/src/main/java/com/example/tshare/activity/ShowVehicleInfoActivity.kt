package com.example.tshare.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.tshare.R
import com.example.tshare.preferenceHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class showVehicleInfoActivity : AppCompatActivity() {
    private lateinit var closebutton: FloatingActionButton
    private lateinit var vehiclePhotoImageView: ShapeableImageView

    private lateinit var make: TextView
    private lateinit var model: TextView
    private lateinit var owner: TextView
    private lateinit var fuelType: TextView
    private lateinit var regNo: TextView
    private var preferenceHelper: preferenceHelper? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_vehicle_info)
        closebutton= findViewById(R.id.close_button)
        make= findViewById(R.id.make)
        model= findViewById(R.id.model)
        owner= findViewById(R.id.owner)
        fuelType= findViewById(R.id.fuelType)
        regNo= findViewById(R.id.regNo)
        preferenceHelper =  preferenceHelper(applicationContext)


        closebutton?.setOnClickListener {
            finish()
        }

        val uid= preferenceHelper!!.getString("vehicleOwnerId","")
        if(uid!=null){

            val database = FirebaseDatabase.getInstance()
            val vehiclePath = "vehicle/$uid"
            val currentUserRef = database.getReference(vehiclePath)
            currentUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
                        // Retrieve the username for the current user
                        val vehicleName = dataSnapshot.child("vehicleName").getValue(String::class.java)
                        val registrationNumber= dataSnapshot.child("registrationNumber").getValue(String::class.java)
                        val photo = dataSnapshot.child("photo").getValue(String::class.java)
                        val fireOwner= dataSnapshot.child("owner").getValue(String::class.java)
                        val fireModel= dataSnapshot.child("model").getValue(String::class.java)
                        val fireFuelType= dataSnapshot.child("fuelType").getValue(String::class.java)
                        if(!vehicleName.isNullOrEmpty()  ){
                            make.setText(vehicleName)
                        }
                        if(!registrationNumber.isNullOrEmpty()){
                            regNo.setText(registrationNumber)
                        }
                        if(!fireOwner.isNullOrEmpty()){
                            owner.setText(fireOwner)
                        }
                        if(!fireModel.isNullOrEmpty()){
                            model.setText(fireModel)
                        }
                        if(!fireFuelType.isNullOrEmpty()){
                            fuelType.setText(fireFuelType)
                        }
                        if(!photo.isNullOrEmpty()){
                            loadVehicleImage(photo)
                        }

                    }else{
                        make.setText(("User doesn't have added this detail "))
                        model.setText(("User doesn't have added this detail"))
                        regNo.setText(("User doesn't have added this detail"))
                        owner.setText(("User doesn't have added this detail"))
                        fuelType.setText(("User doesn't have added this detail"))
                        Log.e("FirebaseData", "Data does not exist for the current user")

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FirebaseData", "Error reading data from Firebase: ${databaseError.message}")

                }
            })
        }

    }
    private fun loadVehicleImage(vehicleImageReference: String) {
        vehiclePhotoImageView = findViewById(R.id.vehicleImage)

        val profilePictureUrl = vehicleImageReference

        if (!profilePictureUrl.isNullOrEmpty()) {
            // Use Glide to load the image into the ImageView
            Glide.with(vehiclePhotoImageView.context)
                .load(profilePictureUrl)
                .placeholder(R.drawable.icon_image) // Placeholder image
                .error(R.drawable.icon_image)       // Error image if loading fails
                .into(vehiclePhotoImageView)
        } else {
            // Handle the case where the URL is null or empty
            Log.e("showVehicleInfoActivity", "Empty or null image URL")
        }
    }



}