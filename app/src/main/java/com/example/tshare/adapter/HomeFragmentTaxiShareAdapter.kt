package com.example.tshare.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tshare.R
import com.example.tshare.activity.ChatActivity
import com.example.tshare.model.recyclerTaxiShare
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class homeFragmentTaxiShareAdapter(private val offerList : ArrayList<recyclerTaxiShare>) : RecyclerView.Adapter<homeFragmentTaxiShareAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.taxi_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  offerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = offerList[position]

        holder.aFrom.text = currentitem.from
        holder.aTo.text = currentitem.to
        holder.aDate.text = currentitem.date
        holder.aTime.text = currentitem.time
        holder.aTimezone.text = currentitem.timeZone
        holder.aPreference.text = currentitem.preference

        holder.map_button?.setOnClickListener {
            val source = currentitem.from.toString()
            val destination = currentitem.to.toString()

            if (source.isEmpty() || destination.isEmpty()) {
                Toast.makeText(holder.itemView.context, "Source and destination are empty", Toast.LENGTH_LONG).show()
            } else {
                val uri = Uri.parse("http://www.google.com/maps/dir/$source/$destination")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                holder.itemView.context.startActivity(intent)
            }
        }
        holder.chat_button?.setOnClickListener {
            val userId = currentitem.userId.toString()
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("UserId", userId)
            holder.itemView.context.startActivity(intent)
        }
        val id= currentitem.userId.toString()
        if(!id.equals("")){
            val profilePictureRef = FirebaseDatabase.getInstance().getReference("users/$id/profilePicture")
            profilePictureRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val profilePictureUrl = dataSnapshot.value.toString()

                    // Use Glide to load the image into the ImageView
                    Glide.with(holder.itemView.context)
                        .load(profilePictureUrl)
//                    .placeholder(R.drawable.profile_photo) // Placeholder image
//                    .error(R.drawable.default_profile_image)       // Error image if loading fails
                        .into(holder.proPic)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.e("ProfileFragment", "Error loading profile picture", databaseError.toException())
                }
            })

        }


    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val  aFrom : TextView = itemView.findViewById(R.id.taxiFrom)
        val  aTo : TextView = itemView.findViewById(R.id.taxiTo)
        val  aDate : TextView = itemView.findViewById(R.id.taxiDate)
        val  aTime : TextView = itemView.findViewById(R.id.taxiTime)
        val  aTimezone: TextView = itemView.findViewById(R.id.taxiTimeZone)
        val  aPreference: TextView = itemView.findViewById(R.id.taxiPreference)
        val map_button: Button = itemView.findViewById(R.id.taxiRoute)
        val proPic: ShapeableImageView= itemView.findViewById(R.id.propic)
        val chat_button: Button= itemView.findViewById(R.id.reChat)

    }
}