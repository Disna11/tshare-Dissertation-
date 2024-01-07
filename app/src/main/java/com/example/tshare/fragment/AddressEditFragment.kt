package com.example.tshare.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tshare.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class addressEditFragment : Fragment() {
    private lateinit var addressEdt:EditText
    private lateinit var  addressUpdateButton: Button
    private lateinit var auth: FirebaseAuth

    var user: FirebaseUser?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_last_name_edit, container, false)
        val closeButton = view.findViewById<FloatingActionButton>(R.id.close_button)
        closeButton?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        auth = FirebaseAuth.getInstance()
        addressEdt=view.findViewById(R.id.lastnameUpdate)
        addressUpdateButton=view.findViewById(R.id.lastnameUpdateButton)

        user=auth.currentUser
        if (user != null) {
            addressUpdateButton?.setOnClickListener {
                if (addressEdt.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Address Field is Empty", Toast.LENGTH_SHORT).show()
                } else {
                    updateAddress(addressEdt.text.toString())
                }
            }
        } else {

            Log.e("ProfileFragment", "User not authenticated")
        }
        addressUpdateButton?.setOnClickListener {
            if (addressEdt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Lastname Field is Empty", Toast.LENGTH_SHORT).show()
            }
            else{
                updateAddress(addressEdt.text.toString())
            }
        }


        return view
    }


    private fun updateAddress(addr: String){
        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().currentUser?.uid + "/address").setValue(addr).
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Database update successful
                Toast.makeText(requireContext(), "Address Updated", Toast.LENGTH_SHORT).show()
                Log.d("ProfileFragment", "Database update successful")
                requireActivity().supportFragmentManager.popBackStack()

            } else {
                // Database update failed
                Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show()
                Log.e("ProfileFragment", "Failed to update database", task.exception)
            }
        }

    }


}