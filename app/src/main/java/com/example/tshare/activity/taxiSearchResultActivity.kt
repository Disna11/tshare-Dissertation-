package com.example.tshare.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tshare.R
import com.example.tshare.adapter.rideSearchAdapter
import com.example.tshare.adapter.taxiSearchAdapter
import com.example.tshare.model.recyclerOffers
import com.example.tshare.model.recyclerTaxiShare
import com.example.tshare.preferenceHelper
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class taxiSearchResultActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var carOffersRecyclerview: RecyclerView
    private lateinit var carOffersArraylist: ArrayList<recyclerTaxiShare>
    private var preferenceHelper: preferenceHelper? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taxi_search_result)

        carOffersRecyclerview=findViewById(R.id.taxiRequests)
        carOffersRecyclerview.layoutManager= LinearLayoutManager(this)
        carOffersRecyclerview.setHasFixedSize(true)
        preferenceHelper =  preferenceHelper(this)
        carOffersArraylist= arrayListOf<recyclerTaxiShare>()
        getOffers()
    }

    private fun getOffers() {
        dbref = FirebaseDatabase.getInstance().getReference("taxiRequests")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {

            val intentFrom = intent.getStringExtra("taxiFrom")
            val intentTo = intent.getStringExtra("taxiTo")
            val intentDate = intent.getStringExtra("taxiDate")

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val offers = userSnapshot.getValue(recyclerTaxiShare::class.java)
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
                    carOffersRecyclerview.adapter = taxiSearchAdapter(carOffersArraylist, preferenceHelper!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }

        })
    }
}