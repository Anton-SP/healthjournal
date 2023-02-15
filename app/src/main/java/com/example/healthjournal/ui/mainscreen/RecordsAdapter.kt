package com.example.healthjournal.ui.mainscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthjournal.data.Record
import com.example.healthjournal.databinding.ItemRecordBinding

class RecordsAdapter : RecyclerView.Adapter<RecordsAdapter.RecordViewHolder>() {
    private val records = ArrayList<Record>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordsAdapter.RecordViewHolder {

        val binding = ItemRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecordViewHolder(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: RecordsAdapter.RecordViewHolder, position: Int) {
        holder.bind(record = records[position])
    }

    override fun getItemCount(): Int {
        return records.size
    }

    fun submitList(data: List<Record>) {
        records.clear()
        records.addAll(data)
        notifyDataSetChanged()
    }

    inner class RecordViewHolder(
        private val binding: ItemRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: Record) {
            binding.apply {
                tvTime.text = record.time
                tvPressurePart1.text = record.pressure1.toString()
                tvPressurePart2.text = record.pressure2.toString()
                tvPulse.text = record.pulse.toString()
            }
        }
    }
}