package com.example.tshare

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class rideOfferFragment : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var carOffersRecyclerview: RecyclerView
    private lateinit var carOffersArraylist: ArrayList<recyclerOffers>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_ride_offer, container, false)
        carOffersRecyclerview=view.findViewById(R.id.offers)
        carOffersRecyclerview.layoutManager= LinearLayoutManager(requireActivity())
        carOffersRecyclerview.setHasFixedSize(true)

        carOffersArraylist= arrayListOf<recyclerOffers>()
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
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val dateInString=offers?.date
                        val firebaseDate = dateFormat.parse(dateInString)
                        val calendar = Calendar.getInstance()
                        calendar.time = firebaseDate
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

                        val midnightFirebaseDate = calendar.time
                        val currentDate = Calendar.getInstance().time
                        val midnightCurrentDate = Calendar.getInstance().apply {
                            time = currentDate
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }.time


                        if (midnightFirebaseDate >= midnightCurrentDate) {
                            carOffersArraylist.add(offers!!)
                        }
                    }

                    carOffersRecyclerview.adapter= homeFragmentCarAdapter(carOffersArraylist)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }

        })
    }




}