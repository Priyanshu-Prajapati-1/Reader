package com.example.reader.model.bookModel

import androidx.compose.runtime.Immutable

@Immutable
data class ReadingModes(
    val image: Boolean,
    val text: Boolean
)