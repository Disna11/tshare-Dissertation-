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


class lastNameEditFragment : Fragment() {

    private lateinit var lnameEdt:EditText
    private lateinit var  updateButton: Button
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


        if (user != null) {
            updateButton?.setOnClickListener {
                if (lnameEdt.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Lastname Field is Empty", Toast.LENGTH_SHORT).show()
                } else {
                    updateLastName(lnameEdt.text.toString())
                }
            }
        } else {

            Log.e("ProfileFragment", "User not authenticated")
        }
        lnameEdt=view.findViewById(R.id.lastnameUpdate)
        updateButton=view.findViewById(R.id.lastnameUpdateButton)

        user=auth.currentUser
        updateButton?.setOnClickListener {
            if (lnameEdt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Lastname Field is Empty", Toast.LENGTH_SHORT).show()
            }
            else{
                updateLastName(lnameEdt.text.toString())
            }
        }



        return view
    }

    private fun updateLastName(lName: String){
        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().currentUser?.uid + "/lastname").setValue(lName).
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Database update successful
                Toast.makeText(requireContext(), "Last Name Updated", Toast.LENGTH_SHORT).show()
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