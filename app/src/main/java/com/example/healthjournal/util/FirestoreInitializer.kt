package com.example.healthjournal.util

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreInitializer : Initializer<FirebaseFirestore> {
    override fun create(context: Context): FirebaseFirestore {
        val firestore = Firebase.firestore
        return firestore
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
       return mutableListOf()
    }
}