package com.example.tshare.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tshare.R
import com.example.tshare.adapter.homeFragmentTaxiShareAdapter
import com.example.tshare.model.recyclerTaxiShare
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class taxiShareFragment : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var taxiShareRecyclerview: RecyclerView
    private lateinit var taxiShareArraylist: ArrayList<recyclerTaxiShare>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_taxi_share, container, false)
        taxiShareRecyclerview=view.findViewById(R.id.taxiRequests)
        taxiShareRecyclerview.layoutManager= LinearLayoutManager(requireActivity())
        taxiShareRecyclerview.setHasFixedSize(true)

        taxiShareArraylist= arrayListOf<recyclerTaxiShare>()
        getOffers()


        return view
    }

    private fun getOffers() {
        dbref= FirebaseDatabase.getInstance().getReference("taxiRequests")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val taxiRequests= userSnapshot.getValue(recyclerTaxiShare::class.java)
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val dateInString=taxiRequests?.date
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
                            taxiShareArraylist.add(taxiRequests!!)
                        }
                    }

                    taxiShareRecyclerview.adapter= homeFragmentTaxiShareAdapter(taxiShareArraylist)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }

        })
    }


}