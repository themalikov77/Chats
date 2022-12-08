package com.example.chats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chats.databinding.ItemChatBinding
import com.google.firebase.firestore.auth.User

class ChatAdapter() :
    RecyclerView.Adapter<ChatAdapter.ChatsViewHolder>() {
    private var users: ArrayList<com.example.chats.model.User> = arrayListOf()
    fun addUsers(newUsers:ArrayList<com.example.chats.model.User>){
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        return ChatsViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }


    inner class ChatsViewHolder(var binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: com.example.chats.model.User) {
            binding.tvUserName.text = user.userName
            binding.tvNumber.text = user.phone
        }

    }
}