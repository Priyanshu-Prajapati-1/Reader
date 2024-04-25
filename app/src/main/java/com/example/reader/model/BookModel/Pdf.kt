package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable

@Immutable
data class Pdf(
    val acsTokenLink: String,
    val isAvailable: Boolean
)