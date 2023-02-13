package com.example.healthjournal.util

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthInitializer: Initializer<FirebaseAuth> {
    override fun create(context: Context): FirebaseAuth {
        val firebaseAuth = Firebase.auth
        return  firebaseAuth
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
       return mutableListOf()
    }
}