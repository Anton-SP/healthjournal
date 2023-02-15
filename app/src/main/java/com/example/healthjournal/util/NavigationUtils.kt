package com.example.healthjournal.util

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

const val NAV_BUNDLE_KEY = "nav_bundle_key"

fun Fragment.navigate(actionId: Int, data: String? = null) {
    val navController = findNavController()
    val bundle = Bundle().apply {
        putString(NAV_BUNDLE_KEY, data)
    }
    navController.navigate(actionId, bundle)
}

val Fragment.navigationData: String?
    get() = arguments?.getString(NAV_BUNDLE_KEY)

