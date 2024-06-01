package com.example.reader.model.bookModel

import androidx.compose.runtime.Immutable

@Immutable
data class Pdf(
    val acsTokenLink: String,
    val isAvailable: Boolean
)