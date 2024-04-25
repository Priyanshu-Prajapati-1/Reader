package com.example.reader.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reader.data.Resource
import com.example.reader.model.BookModel.Item
import com.example.reader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository) :
    ViewModel() {

    var list: List<Item> by mutableStateOf(listOf())

    var isLoading: Boolean by mutableStateOf(true)

    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        isLoading = true
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                return@launch
            }
            try {
                when (val response = repository.getBooks(query)) {
                    is Resource.Success -> {
                        list = response.data!!
                        if (list.isNotEmpty()) {
                            isLoading = false
                        }
                    }

                    is Resource.Error -> {
                        isLoading = false
                        Log.d("Error", "Error fetching books :")
                    }

                    else -> {
                        isLoading = false
                    }
                }
            } catch (exception: Exception) {
                isLoading = false
                Log.d("error", "searchBooks : ${exception.message.toString()}")
            }
        }
    }


//    val listOfBooks: MutableState<DataOrException<List<Item>, Boolean, Exception>> =
//        mutableStateOf(DataOrException(null, true, Exception("")))
//
//    init {
//        searchBooks("android")
//    }
//
//    fun searchBooks(query: String) {
//        viewModelScope.launch {
//            if (query.isEmpty()) {
//                return@launch
//            }
//            listOfBooks.value.loading = true
//            listOfBooks.value = repository.getBooks(query)
//
//            Log.d("tag", "search books: ${listOfBooks.value.data.toString()}")
//
//            if (listOfBooks.value.data.toString().isNotEmpty()) {
//                listOfBooks.value.loading = false
//            }
//        }
//    }
}