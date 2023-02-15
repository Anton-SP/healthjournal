package com.example.healthjournal.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthjournal.data.Record
import com.example.healthjournal.ui.RecordListState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainFragmentViewModel(private val db: FirebaseFirestore, private val userID:String) : ViewModel() {

    var isSigningIn: Boolean = false

    private val listState =
        MutableStateFlow<RecordListState>(RecordListState.Loading)

    fun getStateFlow() = listState.asStateFlow()

    private var records = mutableListOf<Record>()


    fun getList() {
        viewModelScope.launch() {
            try {
                 db.collection(userID)
                    .get()
                    .addOnSuccessListener { result ->
                        records.clear()
                     records =  result.toObjects(Record::class.java)
                    }
                listState.emit(
                    RecordListState.ListSuccess(data = records)
                )
                if (records.isEmpty()) {
                    listState.emit(RecordListState.Error(MESSAGE_EMPTY_LIST))
                }
            } catch (e: Exception) {
                listState.emit(RecordListState.Error(MESSAGE_LIST_ERROR))
            }
        }
    }

    companion object {
        const val MESSAGE_DONT_FIND_MARK = "Cannot find records"
        const val MESSAGE_EMPTY_LIST = "Records list is empty"
        const val MESSAGE_LIST_ERROR = "Loading error"
    }

    class MainFragmentViewModelFactory(private val db: FirebaseFirestore,private val userID: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainFragmentViewModel(db,userID) as T
    }

}