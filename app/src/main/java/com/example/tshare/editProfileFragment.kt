package com.example.tshare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class editProfileFragment : Fragment() {
    private lateinit var editMail: TextView
    private lateinit var firstName: TextView
     var vemail:String?=null
    var vfirstName:String?=null
    val defaultValue = ""
    var preferenceHelper: preferenceHelper? =null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val closeButton = view.findViewById<FloatingActionButton>(R.id.close_button)
        editMail=view.findViewById(R.id.editEmail)
        firstName=view.findViewById(R.id.editfirstName)
        preferenceHelper = preferenceHelper(requireContext())
        vemail= preferenceHelper!!.getString("logedin_email", defaultValue)
        vfirstName= preferenceHelper!!.getString("firstname", defaultValue)


        closeButton?.setOnClickListener {
            // Navigate back to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }
        editMail.setText(vemail)
        firstName.setText(vfirstName)


        return view
    }


}