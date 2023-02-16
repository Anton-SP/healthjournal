package com.example.healthjournal.ui

import com.example.healthjournal.data.Record

sealed class RecordListState {

    data class ListSuccess(val data: List<Record>) : RecordListState()

    object DeleteSuccess : RecordListState()

    data class Error(val message: String) : RecordListState()

    object Loading : RecordListState()
}