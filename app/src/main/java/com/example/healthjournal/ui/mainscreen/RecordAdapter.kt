package com.example.healthjournal.ui.mainscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthjournal.data.Record
import com.example.healthjournal.databinding.ItemRecordBinding
import com.example.healthjournal.ui.FirestoreAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

open class RecordAdapter(query: Query, private val listener: OnRecordSelectedListener) :
    FirestoreAdapter<RecordAdapter.RecordViewHolder>(query) {

    interface OnRecordSelectedListener {

        fun onRecordSelectedListener(record: DocumentSnapshot)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordAdapter.RecordViewHolder {
        return RecordViewHolder(
            ItemRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordAdapter.RecordViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    inner class RecordViewHolder(val binding: ItemRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            snapshot: DocumentSnapshot,
            listener: OnRecordSelectedListener?
        ) {
            val record = snapshot.toObject<Record>()
            if (record == null) {
                return
            }

            val resources = binding.root.resources

            binding.apply {
                tvTime.text = record.time
                tvPressurePart1.text = record.pressure1.toString()
                tvPressurePart2.text = record.pressure2.toString()
                tvPulse.text = record.pulse.toString()
            }

            binding.root.setOnClickListener {
                listener?.onRecordSelectedListener(snapshot)
            }
        }
    }


}