package com.example.healthjournal

import android.app.Application
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class App :Application() {

    val firestore by lazy {Firebase.firestore}

}

val Context.app:App get( ) = applicationContext as App