package com.example.reader.screens.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.reader.data.Resource
import com.example.reader.model.BookModel.Item
import com.example.reader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val bookRepository: BookRepository): ViewModel(){

    suspend fun getBookInfo(bookId : String): Resource<Item>{
        return bookRepository.getBookInfo(bookId)
    }

}