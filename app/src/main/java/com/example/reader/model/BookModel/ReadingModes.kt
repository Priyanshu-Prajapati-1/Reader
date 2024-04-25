package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable

@Immutable
data class ReadingModes(
    val image: Boolean,
    val text: Boolean
)