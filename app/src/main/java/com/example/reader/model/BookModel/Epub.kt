package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable


@Immutable
data class Epub(
    val acsTokenLink: String,
    val isAvailable: Boolean
)