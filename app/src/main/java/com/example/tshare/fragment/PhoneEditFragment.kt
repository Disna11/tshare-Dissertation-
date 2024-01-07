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

class phoneEditFragment : Fragment() {

    private lateinit var phoneEdt: EditText
    private lateinit var phoneUpdateButton: Button
    private lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone_edit, container, false)
        val closeButton = view.findViewById<FloatingActionButton>(R.id.close_button)
        phoneEdt = view.findViewById(R.id.phoneUpdate)
        phoneUpdateButton = view.findViewById(R.id.phoneUpdateButton)

        closeButton?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        phoneUpdateButton?.setOnClickListener {
            val phonenum: String = phoneEdt.text.toString()

            if (user != null) {
                if (phonenum.isEmpty()) {
                    Toast.makeText(requireContext(), "Phone number is Empty", Toast.LENGTH_SHORT).show()
                } else {
                    updatePhone(phonenum)
                }
            } else {
                Log.e("ProfileFragment", "User not authenticated")
            }
        }

        return view
    }

    private fun updatePhone(ph: String) {
        FirebaseDatabase.getInstance().getReference("users/${FirebaseAuth.getInstance().currentUser?.uid}/phone")
            .setValue(ph)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Database update successful
                    Toast.makeText(requireContext(), "Phone Number Updated", Toast.LENGTH_SHORT).show()
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
