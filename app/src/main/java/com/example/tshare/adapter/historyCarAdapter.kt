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
import com.example.tshare.activity.updateRideActivity
import com.example.tshare.model.recyclerOffers
import com.example.tshare.preferenceHelper
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class historyCarAdapter(private val offerList : ArrayList<recyclerOffers>,
                        private val preferenceHelper: preferenceHelper,
                        private val itemIds: ArrayList<String>
) : RecyclerView.Adapter<historyCarAdapter.MyViewHolder>() {

    private lateinit var dbref: DatabaseReference
    private var instance: historyCarAdapter? = null


    // Method to get the singleton instance

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView= LayoutInflater.from(parent.context).inflate(R.layout.history_offers_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  offerList.size
    }

    fun  updateList()
    {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position <offerList.size && position < itemIds.size) {
            val currentitem = offerList[position]
            val itemId = itemIds[position]
            val date = currentitem.date

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
                    Toast.makeText(
                        holder.itemView.context,
                        "Source and destination are empty",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val uri = Uri.parse("http://www.google.com/maps/dir/$source/$destination")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setPackage("com.google.android.apps.maps")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    holder.itemView.context.startActivity(intent)
                }
            }

            holder.deleteBtn?.setOnClickListener {

                dbref = FirebaseDatabase.getInstance().getReference("seatAvailability")

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
                Log.d(
                    "AdapterDebug",
                    "Before Deletion - offerList size: ${offerList.size}, itemIds size: ${itemIds.size}"
                )
// Deletion logic
                Log.d(
                    "AdapterDebug",
                    "After Deletion - offerList size: ${offerList.size}, itemIds size: ${itemIds.size}"
                )

                if (midnightFirebaseDate < midnightCurrentDate) {

                    Toast.makeText(holder.itemView.context, "Could not delete", Toast.LENGTH_LONG)
                        .show()

                } else {
                    dbref.child(itemId).removeValue().addOnSuccessListener {
                        Toast.makeText(
                            holder.itemView.context,
                            "successfully deleted",
                            Toast.LENGTH_LONG
                        ).show()
                        offerList.remove(currentitem)
                        notifyDataSetChanged()


                    }.addOnFailureListener {
                        Toast.makeText(
                            holder.itemView.context,
                            "failed to delete",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

            }

            holder.updateBtn?.setOnClickListener {
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
                    val myIntent = Intent(holder.itemView.context, updateRideActivity::class.java)
                    myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    val userId=currentitem.userId
                    myIntent.putExtra("itemId", itemId)
                    myIntent.putExtra("userId",userId)
                    holder.itemView.context.startActivity(myIntent)
                }

            }

            val id = currentitem.userId.toString()
            if (!id.equals("")) {
                val profilePictureRef =
                    FirebaseDatabase.getInstance().getReference("users/$id/profilePicture")
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
                        Log.e(
                            "historyCarAdapter",
                            "Error loading database",
                            databaseError.toException()
                        )
                    }
                })

            }
        }

    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val  aFrom : TextView = itemView.findViewById(R.id.reFrom)
        val  aTo : TextView = itemView.findViewById(R.id.reTo)
        val  aDate : TextView = itemView.findViewById(R.id.reDate)
        val  aTime : TextView = itemView.findViewById(R.id.reTime)
        val  aTimezone: TextView = itemView.findViewById(R.id.reTimeZone)
        val  aPreference: TextView = itemView.findViewById(R.id.rePreference)
        val proPic: ShapeableImageView=itemView.findViewById(R.id.propic)
        val map_button: Button = itemView.findViewById(R.id.reRoute)
        val deleteBtn: Button = itemView.findViewById(R.id.reDeleteButton)
        val updateBtn: Button = itemView.findViewById(R.id.reUpdateButton)


    }


}