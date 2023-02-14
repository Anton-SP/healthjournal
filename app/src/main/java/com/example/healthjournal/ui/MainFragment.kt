package com.example.healthjournal.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.healthjournal.R
import com.example.healthjournal.databinding.FragmentMainBinding
import com.example.healthjournal.util.navigate
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding:FragmentMainBinding by viewBinding()

    lateinit var firestore: FirebaseFirestore
    private var query: Query? = null

    private lateinit var viewModel:MainFragmentViewModel

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> this.onSignInResult(result) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainFragmentViewModel::class.java)

        FirebaseFirestore.setLoggingEnabled(true)

        firestore = Firebase.firestore

        binding.fabAdd.setOnClickListener {
            navigate(R.id.action_mainFragment_to_newRecord)
        }

    }

    override fun onStart() {
        super.onStart()
        if (shouldStartSignIn()) {
            startSignIn()
            return
        }
    }

    private fun shouldStartSignIn(): Boolean {
      //  return !viewModel.isSigningIn && Firebase.auth.currentUser == null
        return !viewModel.isSigningIn && Firebase.auth.currentUser == null
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
       // viewModel.isSigningIn = false

        if (result.resultCode != Activity.RESULT_OK) {
            if (response == null) {
                // User pressed the back button.
                requireActivity().finish()
            } else if (response.error != null && response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
               // showSignInErrorDialog("R.string.message_no_network")
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
            .setNegativeButton("R.string.option_exit") { _, _ -> requireActivity().finish() }.create()

        dialog.show()
    }

    private fun showTodoToast() {
        Toast.makeText(context, "TODO: Implement", Toast.LENGTH_SHORT).show()
    }

    private fun startSignIn() {
        // Sign in with FirebaseUI
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
            .setIsSmartLockEnabled(false)
            .build()
        signInLauncher.launch(intent)
       // viewModel.isSigningIn = true
    }

}