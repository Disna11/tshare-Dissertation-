package com.example.tshare

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
    private lateinit var closeButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        car= view.findViewById(R.id.carTXT)
        taxi= view.findViewById(R.id.taxiTXT)
        closeButton=view.findViewById(R.id.close_button)

        closeButton?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        car?.setOnClickListener {

        }

        taxi?.setOnClickListener {

        }

        return  view
    }


}