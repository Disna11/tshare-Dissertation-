package com.example.tshare

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class addVehicleInformation : Fragment() {
    private lateinit var vehicleImageView: ShapeableImageView
    private lateinit var editVehiclePhotoButton: FloatingActionButton
    private lateinit var vnamebtn:ImageButton
    private lateinit var  vnametxt:TextView
    private lateinit var  mnamebtn:ImageButton
    private lateinit var  mnametxt:TextView
    private lateinit var fuelTypeTxt:TextView
    private lateinit var registrationTxt:TextView
    private lateinit var ownerTxt:TextView
    private lateinit var licenseTxt:TextView
    private lateinit var fuelTypeBtn:ImageButton
    private lateinit var registrationBtn:ImageButton
    private lateinit var ownerBtn:ImageButton
    private lateinit var licenseBtn:ImageButton


    var preferenceHelper: preferenceHelper? =null
    val defaultValue = ""
    var user: FirebaseUser?=null
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri?=null
    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_add_vehicle_information, container, false)
        auth = Firebase.auth
        vehicleImageView = view.findViewById(R.id.vehicle_photo)
        editVehiclePhotoButton=view.findViewById(R.id.vehicle_add_photo)
        vnamebtn=view.findViewById(R.id.editvehiclename_button)
        vnametxt=view.findViewById(R.id.vehicleName)
        mnametxt=view.findViewById(R.id.modelName)
        fuelTypeTxt=view.findViewById(R.id.fuelType)
        registrationTxt=view.findViewById(R.id.registerNumber)
        ownerTxt=view.findViewById(R.id.owner)
        licenseTxt=view.findViewById(R.id.driverLicense)
        mnamebtn=view.findViewById(R.id.editmodel_button)
        fuelTypeBtn=view.findViewById(R.id.editfuelType_button)
        registrationBtn=view.findViewById(R.id.editregistration_button)
        ownerBtn=view.findViewById(R.id.editowner_button)
        licenseBtn=view.findViewById(R.id.editlicense_button)
        user=auth.currentUser
        preferenceHelper = preferenceHelper(requireContext())
        val currentuid=preferenceHelper!!.getString("userid","")

        if(user==null){
            val myIntent = Intent(requireActivity(), loginActivity::class.java)
            startActivity(myIntent)
            requireActivity().finish()
        }else{
           loadVehicleImage()
        }
        val closeButton = view.findViewById<FloatingActionButton>(R.id.close_button)
        closeButton?.setOnClickListener {
            // Navigate back to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        editVehiclePhotoButton?.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.type = "image/*"
            startActivityForResult(photoIntent, 1)
        }

        vnamebtn.setOnClickListener {
            val vehicleName = EditText(requireContext())
            val vehicleNameDialog = AlertDialog.Builder(requireContext())

            vehicleNameDialog.setTitle("Vehicle Name?")
            vehicleNameDialog.setView(vehicleName)

            // Handle the onClick event
            vehicleNameDialog.setPositiveButton("ADD") { _, _ ->
                val enteredVehicleName = vehicleName.text.toString()
                updateDatabase(enteredVehicleName,"vehicleName")
            }

            // Optionally, you can handle the negative button (e.g., cancel button)
            vehicleNameDialog.setNegativeButton("Cancel") { _, _ ->
            }

            vehicleNameDialog.show()

        }
        mnamebtn.setOnClickListener {
            val modelName = EditText(requireContext())
            val modelNameDialog = AlertDialog.Builder(requireContext())

            modelNameDialog.setTitle("Vehicle Model?")
            modelNameDialog.setView(modelName)

            // Handle the onClick event
            modelNameDialog.setPositiveButton("ADD") { _, _ ->
                val enteredModelName = modelName.text.toString()
                updateDatabase(enteredModelName,"model")
            }

            // Optionally, you can handle the negative button (e.g., cancel button)
            modelNameDialog.setNegativeButton("Cancel") { _, _ ->
            }

           modelNameDialog.show()
        }
        fuelTypeBtn.setOnClickListener {
            val fuelType = EditText(requireContext())
            val fuelTypeDialog = AlertDialog.Builder(requireContext())

            fuelTypeDialog.setTitle("Vehicle's Fuel Type?")
            fuelTypeDialog.setView(fuelType)

            // Handle the onClick event
            fuelTypeDialog.setPositiveButton("ADD") { _, _ ->
                val enteredFuelType = fuelType.text.toString()
                updateDatabase(enteredFuelType,"fuelType")
            }

            // Optionally, you can handle the negative button (e.g., cancel button)
            fuelTypeDialog.setNegativeButton("Cancel") { _, _ ->
            }

            fuelTypeDialog.show()
        }
        registrationBtn.setOnClickListener {
            val regNum = EditText(requireContext())
            val regDialog = AlertDialog.Builder(requireContext())

            regDialog.setTitle("Vehicle Registration Number?")
            regDialog.setView(regNum)

            // Handle the onClick event
            regDialog.setPositiveButton("ADD") { _, _ ->
                val reg = regNum.text.toString()
                updateDatabase(reg,"registrationNumber")
            }

            // Optionally, you can handle the negative button (e.g., cancel button)
            regDialog.setNegativeButton("Cancel") { _, _ ->
            }

            regDialog.show()
        }
        ownerBtn.setOnClickListener {
            val own = EditText(requireContext())
            val ownerDialog = AlertDialog.Builder(requireContext())

            ownerDialog.setTitle("Name of the owner")
            ownerDialog.setView(own)

            // Handle the onClick event
            ownerDialog.setPositiveButton("ADD") { _, _ ->
                val owner = own.text.toString()
                updateDatabase(owner,"owner")
            }

            // Optionally, you can handle the negative button (e.g., cancel button)
            ownerDialog.setNegativeButton("Cancel") { _, _ ->
            }

            ownerDialog.show()
        }
        licenseBtn.setOnClickListener {
            val licence = EditText(requireContext())
            val licenseDialog = AlertDialog.Builder(requireContext())

            licenseDialog.setTitle("License Number")
            licenseDialog.setView(licence)

            // Handle the onClick event
            licenseDialog.setPositiveButton("ADD") { _, _ ->
                val lvalue = licence.text.toString()
                updateDatabase(lvalue,"driverLicense")
            }

            // Optionally, you can handle the negative button (e.g., cancel button)
            licenseDialog.setNegativeButton("Cancel") { _, _ ->
            }

            licenseDialog.show()
        }

        if(currentuid!=null){
            val database = FirebaseDatabase.getInstance()
            val usersPath = "vehicle/$currentuid"
            val currentUserRef = database.getReference(usersPath)
            currentUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
                        // Retrieve the username for the current user
                        val vname = dataSnapshot.child("vehicleName").getValue(String::class.java)
                        val mname= dataSnapshot.child("model").getValue(String::class.java)
                        val fuel= dataSnapshot.child("fuelType").getValue(String::class.java)
                        val reg= dataSnapshot.child("registrationNumber").getValue(String::class.java)
                        val owner= dataSnapshot.child("owner").getValue(String::class.java)
                        val license= dataSnapshot.child("driverLicense").getValue(String::class.java)
                        if(!vname.isNullOrEmpty()  ){
                            vnametxt.setText(vname)
                        }else {
                            vnametxt.setText(("Add Vehicle Name"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                        if(!mname.isNullOrEmpty()){
                            mnametxt.setText(mname)
                        }else{
                            mnametxt.setText(("Add Model"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                        if(!fuel.isNullOrEmpty()){
                            fuelTypeTxt.setText(fuel)
                        }else{
                            fuelTypeTxt.setText(("Add Fuel Type"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                        if(!reg.isNullOrEmpty()){
                            registrationTxt.setText(reg)
                        }else{
                            registrationTxt.setText(("Add registration number"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                        if(!owner.isNullOrEmpty()){
                            ownerTxt.setText(owner)
                        }else{
                            ownerTxt.setText(("who's the Owner"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                        if(!license.isNullOrEmpty()){
                            licenseTxt.setText(license)
                        }else{
                            licenseTxt.setText(("License Number"))
                            Log.e("FirebaseData", "Data does not exist for the current user")
                        }
                    }else{
                        vnametxt.setText(("Add Vehicle Name"))
                        mnametxt.setText(("Add Model"))
                        fuelTypeTxt.setText(("Add Fuel Type"))
                        registrationTxt.setText(("Add registration number"))
                        ownerTxt.setText(("who's the owner"))
                        licenseTxt.setText(("License Number"))
                        Log.e("FirebaseData", "Data does not exist for the current user")

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FirebaseData", "Error reading data from Firebase: ${databaseError.message}")

                }
            })
        }


        return  view
    }




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
        vehicleImageView.setImageBitmap(bitmap)
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
                            updateVehiclrPicture(urlTask.result.toString())
                        }
                    }

                    Toast.makeText(requireContext(), "Image added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to add image of the car!!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateVehiclrPicture(url: String) {
        FirebaseDatabase.getInstance().getReference("vehicle/" + FirebaseAuth.getInstance().currentUser?.uid + "/photo").setValue(url).
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Database update successful
                Log.d("addVehicleInformation", "Database update successful")
            } else {
                // Database update failed
                Log.e("addVehicleInformation", "Failed to update database", task.exception)
            }
        }
    }

    private fun loadVehicleImage() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val vehiclePictureRef = FirebaseDatabase.getInstance().getReference("vehicle/$uid/photo")

        vehiclePictureRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val vehicleImageUrl = dataSnapshot.value.toString()

                // Use Glide to load the image into the ImageView
                Glide.with(requireContext())
                    .load(vehicleImageUrl)
//                    .placeholder(R.drawable.profile_photo) // Placeholder image
//                    .error(R.drawable.default_profile_image)       // Error image if loading fails
                    .into(vehicleImageView)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.e("ProfileFragment", "Error loading profile picture", databaseError.toException())
            }
        })
    }

    private fun updateDatabase(value: String, destination: String) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserUid != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("vehicle/$currentUserUid/$destination")

            databaseReference.setValue(value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Database update successful
                        Toast.makeText(requireContext(), "Data is Updated", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStack()
                        Log.d("ProfileFragment", "Database update successful")

                    } else {
                        // Database update failed
                        Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show()
                        Log.e("ProfileFragment", "Failed to update database", task.exception)
                    }
                }
        }
    }


}