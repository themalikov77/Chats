package com.example.chats.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chats.databinding.FragmentAuthBinding
import com.example.chats.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class AuthFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentAuthBinding
    private var verId = ""
    private var phone = ""
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {


        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.d("ololo", "onVerificationFailed: error " + e.message)
        }

        override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, p1)
            verId = verificationId
            binding.acceptContainer.isVisible = true
            binding.authContainer.isVisible = false
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inAuth.countryCode.setOnCountryChangeListener {
            binding.inAuth.etNumber.setText("+" + binding.inAuth.countryCode.selectedCountryCode)
        }
        auth = FirebaseAuth.getInstance()
        binding.inAuth.btnSend.setOnClickListener {
            Log.d("click", "onViewCreated: ")
            sendPhoneNumber()
        }

        binding.inAccept.btnOk.setOnClickListener {
            val credential =
                PhoneAuthProvider.getCredential(verId, binding.inAccept.etCode.text.toString())
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun sendPhoneNumber() {
        phone = binding.inAuth.etNumber.text.toString()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    saveUserData()
                } else {
                    showToast(task.exception?.message.toString())
                }
            }
    }

    private fun saveUserData() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val ref = FirebaseFirestore.getInstance().collection("Users").document(uid)
            val userData = hashMapOf<String, String>()
            userData["uid"] = uid
            userData["phone"] = phone
            userData["userName"] = binding.inAccept.etUserName.text.toString()
            ref.set(userData).addOnCompleteListener {
                if (it.isSuccessful) {
                    findNavController().navigateUp()
                } else {
                    it.exception?.message?.let { it1 -> showToast(it1) }
                }
            }

        }
    }


}


