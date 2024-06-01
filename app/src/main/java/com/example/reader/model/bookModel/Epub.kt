package com.example.reader.model.bookModel

import androidx.compose.runtime.Immutable


@Immutable
data class Epub(
    val acsTokenLink: String,
    val isAvailable: Boolean
)