package com.example.reader.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reader.data.DataOrException
import com.example.reader.model.MBook
import com.example.reader.repository.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository) :
    ViewModel() {

    private val ifLoading = mutableStateOf(false)

    private val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty()) {
        FirebaseAuth.getInstance().currentUser?.email?.substringBefore("@")
    } else {
        "N/A"
    }

    val data: MutableState<DataOrException<List<MBook>, Boolean, Exception>> =
        mutableStateOf(DataOrException(listOf(), true, Exception("")))

    init {
        getAllBooksFromDatabase()
    }

    private fun getAllBooksFromDatabase() {
        ifLoading.value = true
        viewModelScope.launch {
            try {
                data.value.loading = true
                data.value = repository.getAllBookFromDatabase()
                if (!data.value.data.isNullOrEmpty()) data.value.loading = false
            } catch (e: Exception) {
                data.value = DataOrException(listOf(), false, e)
            }
        }
        // Log.d("data", "getAllBooksFromDatabase: ${data.value.data?.toList().toString()}")
    }

}