package com.example.tshare.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tshare.R
import com.example.tshare.adapter.historyCarAdapter
import com.example.tshare.adapter.homeFragmentCarAdapter
import com.example.tshare.adapter.homeFragmentTaxiShareAdapter
import com.example.tshare.model.recyclerOffers
import com.example.tshare.model.recyclerTaxiShare
import com.example.tshare.preferenceHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class historyRideOfferFragment : Fragment() {
    private lateinit var dbref: DatabaseReference
    private lateinit var carOffersRecyclerview: RecyclerView
    private lateinit var carOffersArraylist: ArrayList<recyclerOffers>
    private lateinit var itemIds: ArrayList<String>
    private var preferenceHelper: preferenceHelper? =null
    private lateinit var auth: FirebaseAuth



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_history_ride_offer, container, false)

        carOffersRecyclerview=view.findViewById(R.id.offers)
        carOffersRecyclerview.layoutManager= LinearLayoutManager(requireActivity())
        carOffersRecyclerview.setHasFixedSize(true)
        auth= Firebase.auth
        preferenceHelper =  preferenceHelper(requireContext())
        carOffersArraylist= arrayListOf<recyclerOffers>()
        itemIds= arrayListOf()
        getOffers()

        return view
    }

    private fun getOffers() {

        dbref= FirebaseDatabase.getInstance().getReference("seatAvailability")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val offers= userSnapshot.getValue(recyclerOffers::class.java)


                        val uid= offers?.userId
                        val currentUser = auth.currentUser
                        val cuid = currentUser?.uid?.toString() ?: ""



                        if (uid== cuid) {
                            carOffersArraylist.add(offers!!)
                            itemIds.add(userSnapshot.key ?: "")
                        }
                    }

                    carOffersRecyclerview.adapter= historyCarAdapter(carOffersArraylist,preferenceHelper!!,itemIds)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }

        })



    }


}