package com.example.healthjournal.ui

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.util.ArrayList

abstract class FirestoreAdapter<VH : RecyclerView.ViewHolder>(private var query: Query) :
    RecyclerView.Adapter<VH>() {

    private var registration: ListenerRegistration? = null

    private val snapshots = ArrayList<DocumentSnapshot>()

    fun startListening() {
        // TODO(developer): Implement
    }

    fun stopListening() {
        registration?.remove()
        registration = null

        snapshots.clear()
        notifyDataSetChanged()
    }

    fun setQuery(query: Query) {
        // Stop listening
        stopListening()

        // Clear existing data
        snapshots.clear()
        notifyDataSetChanged()

        // Listen to new query
        this.query = query
        startListening()
    }

    open fun onError(e: FirebaseFirestoreException) {
        Log.w(TAG, "onError", e)
    }

    open fun onDataChanged() {}

    override fun getItemCount(): Int {
        return snapshots.size
    }

    protected fun getSnapshot(index: Int): DocumentSnapshot {
        return snapshots[index]
    }

    companion object {

        private const val TAG = "HAPPY"
    }
}