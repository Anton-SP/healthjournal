package com.example.healthjournal.data

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Record(
    var time:String,
    var pressure1:Int,
    var pressure2:Int,
    var pulse:Int
)
