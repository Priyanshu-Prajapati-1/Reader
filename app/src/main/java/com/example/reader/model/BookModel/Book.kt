package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable

@Immutable
data class Book(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)