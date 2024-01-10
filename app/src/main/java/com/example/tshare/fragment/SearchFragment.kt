package com.example.tshare.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tshare.R
import com.example.tshare.activity.SearchForCompanionActivity
import com.example.tshare.activity.SearchForRideActivity
import com.example.tshare.activity.bookTaxiActivity

class SearchFragment : Fragment() {
    private  var searchRide:TextView?= null
    private  var searchTaxi:TextView?= null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_search, container, false)

        searchRide=view.findViewById(R.id.rideSearch)
        searchTaxi=view.findViewById(R.id.taxiSearch)

        searchRide?.setOnClickListener {
            val intent = Intent(requireContext(), SearchForRideActivity::class.java)
            startActivity(intent)
        }

        searchTaxi?.setOnClickListener {
            val intent = Intent(requireContext(), SearchForCompanionActivity::class.java)
            startActivity(intent)
        }

        return view
    }


}