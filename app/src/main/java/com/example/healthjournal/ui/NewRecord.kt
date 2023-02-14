package com.example.healthjournal.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.healthjournal.R
import com.example.healthjournal.databinding.FragmentNewRecordBinding

class NewRecord : Fragment(R.layout.fragment_new_record) {

    private val binding:FragmentNewRecordBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOk.setOnClickListener {

        }

    }
}