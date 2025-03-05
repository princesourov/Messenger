package com.example.messenger

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.databinding.UserItemBinding

class UserAdapter(
    //Context
    var  context : Context, var arraylist : ArrayList<Users>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    //ViewHolder
    inner class UserViewHolder(val adapterBinding : UserItemBinding) : RecyclerView.ViewHolder(adapterBinding.root)

    //Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.adapterBinding.Email.text = arraylist[position].name
        holder.adapterBinding.message.text = arraylist[position].message
    }
}