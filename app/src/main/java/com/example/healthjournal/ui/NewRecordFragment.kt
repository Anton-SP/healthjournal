package com.example.healthjournal.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.healthjournal.R
import com.example.healthjournal.app
import com.example.healthjournal.data.Record
import com.example.healthjournal.databinding.FragmentNewRecordBinding
import com.example.healthjournal.util.navigate
import com.example.healthjournal.util.navigationData
import java.util.Calendar


class NewRecordFragment : Fragment(R.layout.fragment_new_record) {

    private val binding: FragmentNewRecordBinding by viewBinding()

    private var userID: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserID()

        binding.btnOk.setOnClickListener {
            saveRecord()
            this.navigate(R.id.action_newRecord_to_mainFragment)
        }

        binding.btnCancel.setOnClickListener {
            this.navigate(R.id.action_newRecord_to_mainFragment)
        }

    }

    private fun getUserID() {
        userID = navigationData
    }

    private fun saveRecord() {

        binding.apply {
            userID?.let { user ->
                requireContext().app.firestore.collection(user).document()
                    .set(
                        Record(
                            time = Calendar.getInstance().time.toString(),
                            pressure1 = edPressurePart1.text.toString().toInt(),
                            pressure2 = edPressurePart2.text.toString().toInt(),
                            pulse = edPulse.text.toString().toInt()
                        )
                    )
                    .addOnSuccessListener { Log.d("HAPPY", "Document successfully written!") }
                    .addOnFailureListener { e -> Log.w("HAPPY", "Error writing document", e) }
            }
        }
    }


}