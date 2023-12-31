package com.example.tshare

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AddFragment : Fragment() {
    private lateinit var car:TextView
    private lateinit var  taxi:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        car= view.findViewById(R.id.carTXT)
        taxi= view.findViewById(R.id.taxiTXT)


        car?.setOnClickListener {
            val intent = Intent(requireContext(), bookCarActivity::class.java)
            startActivity(intent)
        }

        taxi?.setOnClickListener {
            val intent = Intent(requireContext(), bookTaxiActivity::class.java)
            startActivity(intent)
        }

        return  view
    }


}