package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable

@Immutable
data class ListPrice(
    val amount: Double,
    val currencyCode: String
)