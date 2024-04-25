package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable


@Immutable
data class IndustryIdentifier(
    val identifier: String,
    val type: String
)