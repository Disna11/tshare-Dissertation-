package com.example.tshare.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tshare.R
import com.example.tshare.adapter.homeFragmentCarAdapter
import com.example.tshare.adapter.rideSearchAdapter
import com.example.tshare.model.recyclerOffers
import com.example.tshare.preferenceHelper
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class rideSearchResultActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var carOffersRecyclerview: RecyclerView
    private lateinit var carOffersArraylist: ArrayList<recyclerOffers>
    private var preferenceHelper: preferenceHelper? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_search_result)

        carOffersRecyclerview=findViewById(R.id.offerRequests)
        carOffersRecyclerview.layoutManager= LinearLayoutManager(this)
        carOffersRecyclerview.setHasFixedSize(true)
        preferenceHelper =  preferenceHelper(this)
        carOffersArraylist= arrayListOf<recyclerOffers>()
        getOffers()
    }

    private fun getOffers() {
        dbref = FirebaseDatabase.getInstance().getReference("seatAvailability")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {

            val intentFrom = intent.getStringExtra("rideFrom")
            val intentTo = intent.getStringExtra("rideTo")
            val intentDate = intent.getStringExtra("rideDate")

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val offers = userSnapshot.getValue(recyclerOffers::class.java)
                        if (offers != null) {
                            val fireFrom = offers.from
                            val fireTo = offers.to
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val dateInString = offers.date
                            val firebaseDate = dateFormat.parse(dateInString)
                            val intDate = dateFormat.parse(intentDate)

                            if (fireFrom == intentFrom && fireTo == intentTo && firebaseDate != null && intDate != null && firebaseDate == intDate) {
                                carOffersArraylist.add(offers)
                            }
                        }
                    }
                    carOffersRecyclerview.adapter = rideSearchAdapter(carOffersArraylist, preferenceHelper!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }

        })
    }
}