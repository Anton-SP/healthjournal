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
import com.example.healthjournal.databinding.FragmentMainBinding
import com.example.healthjournal.util.navigate
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment(R.layout.fragment_main),RecordAdapter.OnRecordSelectedListener {

    private val binding: FragmentMainBinding by viewBinding()

    private var query: Query? = null

    private lateinit var viewModel: MainFragmentViewModel

    private  var adapter: RecordAdapter? = null

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> this.onSignInResult(result) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainFragmentViewModel::class.java)

        FirebaseFirestore.setLoggingEnabled(true)

        initRecycler()

        binding.rvRecord.layoutManager = LinearLayoutManager(requireContext())

        binding.fabAdd.setOnClickListener {
           val  userID = FirebaseAuth.getInstance().uid.toString()
            Log.d("HAPPY", userID)
            navigate(R.id.action_mainFragment_to_newRecord, userID)
        }

    }

    private fun initRecycler() {
        query?.let {
            adapter = object : RecordAdapter(it, this@MainFragment) {
                override fun onDataChanged() {
                    // Show/hide content if the query returns empty.
                    if (itemCount == 0) {
                        Toast.makeText(requireContext(),"Empty",Toast.LENGTH_SHORT).show()
                      //  binding.rvRecord.visibility = View.GONE
                     //   binding.rvRecord.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(requireContext(),"Got LIST",Toast.LENGTH_SHORT).show()
                     //   binding.rvRecord.visibility = View.VISIBLE
                    //    binding.viewEmpty.visibility = View.GONE
                    }
                }

                override fun onError(e: FirebaseFirestoreException) {
                    // Show a snackbar on errors
                    Snackbar.make(
                        binding.root,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            binding.rvRecord.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        if (shouldStartSignIn()) {
            startSignIn()
            return
        }

        adapter?.startListening()
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
        viewModel.isSigningIn = true
    }

    override fun onRecordSelectedListener(record: DocumentSnapshot) {
        Toast.makeText(requireContext(),"CLICK",Toast.LENGTH_SHORT).show()
    }

}