package com.example.reader.model.BookModel

data class Book(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)