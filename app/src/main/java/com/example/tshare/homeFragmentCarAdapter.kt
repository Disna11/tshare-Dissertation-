package com.example.tshare

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class homeFragmentCarAdapter(private val offerList : ArrayList<recyclerOffers>) : RecyclerView.Adapter<homeFragmentCarAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView= LayoutInflater.from(parent.context).inflate(R.layout.offers_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  offerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val  currentitem= offerList[position]

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
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val  aFrom : TextView = itemView.findViewById(R.id.reFrom)
        val  aTo : TextView = itemView.findViewById(R.id.reTo)
        val  aDate : TextView = itemView.findViewById(R.id.reDate)
        val  aTime : TextView = itemView.findViewById(R.id.reTime)
        val  aTimezone: TextView = itemView.findViewById(R.id.reTimeZone)
        val  aPreference: TextView = itemView.findViewById(R.id.rePreference)
        val map_button: Button = itemView.findViewById(R.id.reRoute)


    }
}