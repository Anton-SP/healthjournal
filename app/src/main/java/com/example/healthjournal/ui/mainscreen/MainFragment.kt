package com.example.healthjournal.ui.mainscreen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.healthjournal.R
import com.example.healthjournal.app
import com.example.healthjournal.data.Record
import com.example.healthjournal.databinding.FragmentMainBinding
import com.example.healthjournal.ui.RecordListState
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
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

//class MainFragment : Fragment(R.layout.fragment_main), RecordAdapter.OnRecordSelectedListener {
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding()

    //  private lateinit var viewModel: MainFragmentViewModel
    private val viewModel: MainFragmentViewModel by viewModels {
        MainFragmentViewModel.MainFragmentViewModelFactory(
            requireContext().app.firestore,
            FirebaseAuth.getInstance().uid.toString()
        )
    }

    private val adapter: RecordsAdapter by lazy { RecordsAdapter() }

   // var repo = mutableListOf<Record>()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> this.onSignInResult(result) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  viewModel = ViewModelProvider(requireActivity()).get(MainFragmentViewModel::class.java)

        FirebaseFirestore.setLoggingEnabled(true)

        initRecycler()

        getRecords()

        collectListFlow()


        binding.fabAdd.setOnClickListener {
           // checkRepo()
            val userID = FirebaseAuth.getInstance().uid.toString()
            Log.d("HAPPY", userID)
            navigate(R.id.action_mainFragment_to_newRecord, userID)
        }

    }

    private fun collectListFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
               viewModel.getStateFlow().collect { state ->
                    checkListState(state)
                }
            }
        }
    }

    private fun checkListState(state: RecordListState) {
        when (state) {
            is RecordListState.Loading -> {
                Toast.makeText(requireContext(), "loading record list", Toast.LENGTH_SHORT).show()
            }
            is RecordListState.ListSuccess -> {
                adapter.submitList(state.data)
            }
            is RecordListState.Error -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
            is RecordListState.DeleteSuccess -> {
                viewModel.getList()
            }

        }
    }

    private fun initRecycler() {
        binding.rvRecord.apply {
            adapter = this@MainFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

  /*  private fun checkRepo() {
        Toast.makeText(requireContext(), repo.size.toString(), Toast.LENGTH_SHORT).show()
    }*/


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

    private fun getRecords(){
        viewModel.getList()
    }

    /*  override fun onRecordSelectedListener(record: DocumentSnapshot) {
          Toast.makeText(requireContext(), "CLICK", Toast.LENGTH_SHORT).show()
      }*/

   /* private fun getRecords() {
        val userID = FirebaseAuth.getInstance().uid.toString()
        requireContext().app.firestore.collection(userID)
            .get()
            .addOnSuccessListener { result ->
                repo.clear()
                repo = result.toObjects(Record::class.java)
            }
        if (repo.size != 0) {
            adapter.submitList(repo)
        }
        //return repo
    }*/


}