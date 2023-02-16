package com.example.healthjournal.ui.mainscreen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.healthjournal.R
import com.example.healthjournal.app
import com.example.healthjournal.data.Record
import com.example.healthjournal.databinding.FragmentMainBinding
import com.example.healthjournal.util.navigate
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase


class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding()

    private lateinit var viewModel: MainFragmentViewModel

    private var query: Query? = null

    private var repository = mutableListOf<Record>()

    private val adapter: RecordsAdapter by lazy { RecordsAdapter() }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> this.onSignInResult(result) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainFragmentViewModel::class.java)

        FirebaseFirestore.setLoggingEnabled(true)
        initRecycler()
        getQuery()

        binding.fabAdd.setOnClickListener {
            val userID = FirebaseAuth.getInstance().uid.toString()
            Log.d("HAPPY", userID)
            navigate(R.id.action_mainFragment_to_newRecord, userID)
        }

    }

    private fun getQuery() {
        requireContext().app.firestore.collection(FirebaseAuth.getInstance().uid.toString())
            .addSnapshotListener(object :
                EventListener<com.google.firebase.firestore.QuerySnapshot> {
                override fun onEvent(
                    value: com.google.firebase.firestore.QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                    repository.clear()
                    for (documents: DocumentChange in value?.documentChanges!!) {
                        if (documents.type == DocumentChange.Type.ADDED) {
                            repository.add(documents.document.toObject(Record::class.java))
                        }
                    }
                    adapter.submitList(repository)
                }

            })
    }

    private fun initRecycler() {

        binding.rvRecord.apply {
            adapter = this@MainFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())

        }
        adapter.submitList(repository)
    }

    override fun onStart() {
        super.onStart()
        if (shouldStartSignIn()) {
            startSignIn()
            return
        }
    }

    private fun shouldStartSignIn(): Boolean {
        return !viewModel.isSigningIn && Firebase.auth.currentUser == null
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        viewModel.isSigningIn = false

        if (result.resultCode != Activity.RESULT_OK) {
            if (response == null) {
                // User pressed the back button.
                requireActivity().finish()
            } else if (response.error != null && response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                showSignInErrorDialog(R.string.message_no_network)
            } else {
                showSignInErrorDialog(R.string.message_unknown)
            }
        }
    }

    private fun showSignInErrorDialog(@StringRes message: Int) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("R.string.title_sign_in_error")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("R.string.option_retry") { _, _ -> startSignIn() }
            .setNegativeButton("R.string.option_exit") { _, _ -> requireActivity().finish() }
            .create()

        dialog.show()
    }

    private fun startSignIn() {
        // Sign in with FirebaseUI
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
            .setIsSmartLockEnabled(false)
            .build()
        signInLauncher.launch(intent)
        viewModel.isSigningIn = true
    }

}