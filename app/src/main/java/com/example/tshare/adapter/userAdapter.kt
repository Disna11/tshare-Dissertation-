package com.example.tshare.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tshare.R
import com.example.tshare.activity.ChatActivity
import com.example.tshare.activity.showVehicleInfoActivity
import com.example.tshare.model.UserWithId
import com.example.tshare.model.chatUser
import com.google.firebase.database.core.Context
import de.hdodenhof.circleimageview.CircleImageView

class userAdapter(private val context: FragmentActivity, private val userList: ArrayList<UserWithId>): RecyclerView.Adapter<userAdapter.ViewHolder> (){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position].chatUser
        holder.txtUserName.text = user?.username ?: "Default Username"
        Glide.with(holder.itemView.context).load(user?.profilePicture).placeholder(R.drawable.icon_profile).into(holder.imgUser)

        holder.layoutUser.setOnClickListener {

                val userId = userList[position].userId
                val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                intent.putExtra("UserId", userId)
                holder.itemView.context.startActivity(intent)

        }


    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtUserName: TextView= view.findViewById(R.id.userName)
        val txtTemp: TextView= view.findViewById(R.id.temp)
        val imgUser: CircleImageView = view.findViewById(R.id.userImage)
        val layoutUser: LinearLayout=view.findViewById(R.id.layoutUser)
    }
}