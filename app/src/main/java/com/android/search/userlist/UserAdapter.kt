package com.android.search.userlist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.search.R
import com.android.search.data.User
import com.bumptech.glide.Glide

private const val TAG = "UserAdapter"

class UserAdapter(mContext: Context) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback) {
    private val context: Context = mContext
    private var userList: ArrayList<User>? = null
    private var userPosition: Int = 0

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userTextView: TextView = itemView.findViewById(R.id.user_text)
        val userImageView: ImageView = itemView.findViewById(R.id.user_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Log.i(TAG, Integer.toString(position))
        userPosition = position
        val user = getItem(position)
        holder.userTextView.text = user.name
        Glide.with(context)
            .load(user.image)
            .into(holder.userImageView);
    }

    fun addUserList(userList: ArrayList<User>){
        this.userList = userList
        notifyDataSetChanged()
    }

    fun deleteUserList(userList: ArrayList<User>){
        this.userList = userList
        notifyDataSetChanged()
    }

    fun getUserPosition(): Int{
        return userPosition
    }

}

object UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        //return oldItem.id == newItem.id
        return oldItem == newItem
    }

}