package com.example.healthjournal.data


import com.google.firebase.firestore.PropertyName


data class Record(
    @get: PropertyName("time") @set: PropertyName("time")
    var time:String="",
    @get: PropertyName("pressure1") @set: PropertyName("pressure1")
    var pressure1:Int=0,
    @get: PropertyName("pressure2") @set: PropertyName("pressure2")
    var pressure2:Int=0,
    @get: PropertyName("pulse") @set: PropertyName("pulse")
    var pulse:Int=0
) {

}



