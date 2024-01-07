package com.example.tshare.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import java.text.SimpleDateFormat
import java.util.*


class dobEditFragment : Fragment() {

    private lateinit var dobEdt: EditText
    private lateinit var  dobUpdateButton: Button
    private lateinit var auth: FirebaseAuth
    var user: FirebaseUser?=null

    val tw: TextWatcher = object : TextWatcher {
        private var current = ""
        private val ddmmyyyy = "DDMMYYYY"
        private val cal = Calendar.getInstance()
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // No specific implementation for beforeTextChanged
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString() != current) {
                var clean = s.toString().replace("[^\\d.]|\\.".toRegex(), "")
                val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")

                var cl = clean.length
                var sel = cl
                var i = 2
                while (i <= cl && i < 6) {
                    sel++
                    i += 2
                }
                // Fix for pressing delete next to a forward slash
                if (clean == cleanC) sel--

                if (clean.length < 8) {
                    clean += ddmmyyyy.substring(clean.length)
                } else {
                    // This part makes sure that when we finish entering numbers
                    // the date is correct, fixing it otherwise
                    var day = clean.substring(0, 2).toInt()
                    var mon = clean.substring(2, 4).toInt()
                    var year = clean.substring(4, 8).toInt()

                    mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                    cal[Calendar.MONTH] = mon - 1
                    year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                    cal[Calendar.YEAR] = year
                    // ^ first set year for the line below to work correctly
                    // with leap years - otherwise, date e.g. 29/02/2012
                    // would be automatically corrected to 28/02/2012

                    day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                    clean = String.format("%02d%02d%02d", day, mon, year)
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8))

                sel = if (sel < 0) 0 else sel
                current = clean
                dobEdt.setText(current)
                dobEdt.setSelection(if (sel < current.length) sel else current.length)
            }
        }


        override fun afterTextChanged(s: Editable) {
            // Implementation of afterTextChanged
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_dob_edit, container, false)
        val closeButton = view.findViewById<FloatingActionButton>(R.id.close_button)
        closeButton?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        auth = FirebaseAuth.getInstance()
        dobEdt=view.findViewById(R.id.dobUpdate)
        dobUpdateButton=view.findViewById(R.id.dobUpdateButton)
        user=auth.currentUser


        dobEdt.addTextChangedListener(tw)




        if (user != null) {
            dobUpdateButton?.setOnClickListener {
                if (dobEdt.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Date Of Birth Field is Empty", Toast.LENGTH_SHORT).show()
                } else {
                    updateDob(dobEdt.text.toString())
                }
            }
        } else {

            Log.e("ProfileFragment", "User not authenticated")
        }

        dobUpdateButton?.setOnClickListener {
            if (dobEdt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Date of Birth Field is Empty", Toast.LENGTH_SHORT).show()
            }
            else{
                val dobInput = dobEdt.text.toString()
                val dob = if (dobInput.isNotEmpty()) {
                    // Use the provided date if not empty
                    dobInput
                } else {
                    // Use the current date if empty
                    val currentDate = Date()
                    SimpleDateFormat("dd-MM-yyyy").format(currentDate)
                }

                updateDob(dob)
            }
        }

        return view
    }

    private fun updateDob(Dob: String){
        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().currentUser?.uid + "/dob").setValue(Dob).
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Database update successful
                Toast.makeText(requireContext(), "Date of Birth Updated", Toast.LENGTH_SHORT).show()
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