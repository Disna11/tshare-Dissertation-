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
import com.example.tshare.activity.updateTaxiActivity
import com.example.tshare.model.recyclerTaxiShare
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class historyTaxiShareAdapter(private val offerList : ArrayList<recyclerTaxiShare>,
                              private val itemIds: ArrayList<String>)
                                : RecyclerView.Adapter<historyTaxiShareAdapter.MyViewHolder>() {

    private lateinit var dbref: DatabaseReference




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.history_taxi_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  offerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = offerList[position]
        val itemId=itemIds[position]
        val date=currentitem.date

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
        holder.delete?.setOnClickListener {
            dbref= FirebaseDatabase.getInstance().getReference("taxiRequests")

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val firebaseDate = dateFormat.parse(date)
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
            if(midnightFirebaseDate < midnightCurrentDate){

                Toast.makeText(holder.itemView.context,"Could not delete",Toast.LENGTH_LONG).show()

            }else{

                dbref.child(itemId).removeValue().addOnSuccessListener {
                    Toast.makeText(holder.itemView.context,"successfully deleted",Toast.LENGTH_LONG).show()

                   offerList.remove(currentitem)
                        notifyDataSetChanged()
                }.addOnFailureListener {
                    Toast.makeText(holder.itemView.context,"failed to delete",Toast.LENGTH_LONG).show()

                }
            }
        }
        holder.update?.setOnClickListener {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val firebaseDate = dateFormat.parse(date)
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
            if(midnightFirebaseDate < midnightCurrentDate){

                Toast.makeText(holder.itemView.context,"Could not update",Toast.LENGTH_LONG).show()

            }else{
                val myIntent = Intent(holder.itemView.context, updateTaxiActivity::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                val userId=currentitem.userId
                myIntent.putExtra("itemId", itemId)
                myIntent.putExtra("userId",userId)
                holder.itemView.context.startActivity(myIntent)
            }



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
        val delete: Button= itemView.findViewById(R.id.reDelete)
        val update: Button= itemView.findViewById(R.id.reUpdate)

    }
}