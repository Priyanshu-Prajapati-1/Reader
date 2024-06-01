package com.example.reader.model.bookModel

import androidx.compose.runtime.Immutable


@Immutable
data class IndustryIdentifier(
    val identifier: String,
    val type: String
)