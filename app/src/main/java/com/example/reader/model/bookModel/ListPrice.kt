package com.example.reader.model.bookModel

import androidx.compose.runtime.Immutable

@Immutable
data class ListPrice(
    val amount: Double,
    val currencyCode: String
)