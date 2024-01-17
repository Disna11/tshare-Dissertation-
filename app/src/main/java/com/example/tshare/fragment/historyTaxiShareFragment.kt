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
import com.example.tshare.adapter.historyTaxiShareAdapter
import com.example.tshare.adapter.homeFragmentTaxiShareAdapter
import com.example.tshare.model.recyclerTaxiShare
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class historyTaxiShareFragment : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var taxiShareRecyclerview: RecyclerView
    private lateinit var taxiShareArraylist: ArrayList<recyclerTaxiShare>
    private lateinit var itemIds: ArrayList<String>
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_history_taxi_share, container, false)

        taxiShareRecyclerview=view.findViewById(R.id.taxiRequests)
        taxiShareRecyclerview.layoutManager= LinearLayoutManager(requireActivity())
        taxiShareRecyclerview.setHasFixedSize(true)

        taxiShareArraylist= arrayListOf<recyclerTaxiShare>()
        itemIds = arrayListOf()
        auth= Firebase.auth
        getOffers()
        return view
    }

    private fun getOffers() {
        dbref= FirebaseDatabase.getInstance().getReference("taxiRequests")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                taxiShareArraylist.clear()
                itemIds.clear()
                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val taxiRequests= userSnapshot.getValue(recyclerTaxiShare::class.java)
                        val uid= taxiRequests?.userId
                        val currentUser = auth.currentUser
                        val cuid = currentUser?.uid?.toString() ?: ""
                        if (uid== cuid) {
                            taxiShareArraylist.add(taxiRequests!!)
                            itemIds.add(userSnapshot.key ?: "")
                        }
                    }

                    taxiShareRecyclerview.adapter= historyTaxiShareAdapter(taxiShareArraylist,itemIds)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }

        })


    }


}