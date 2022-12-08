package com.example.chats.ui.chats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chats.R
import com.example.chats.adapter.ChatAdapter
import com.example.chats.databinding.FragmentChatsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.lang.ProcessBuilder.Redirect.to

class ChatsFragment : Fragment() {
    private lateinit var binding: FragmentChatsBinding
    private val users = arrayListOf<com.example.chats.model.User>()
    private lateinit var adapter: ChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ref = FirebaseFirestore.getInstance().collection("Users")
        adapter = ChatAdapter()
        binding.recycler.adapter = adapter
        ref.get().addOnCompleteListener {
            Log.d("ololo", "onViewCreated: " + it)
        }
        ref.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (item in it.result.documents) {
                    val user = item.toObject(com.example.chats.model.User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                adapter.addUsers(users)
            }
        }
    }
}
