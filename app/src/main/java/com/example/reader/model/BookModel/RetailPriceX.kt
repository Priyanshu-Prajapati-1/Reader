package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable

@Immutable
data class RetailPriceX(
    val amount: Double,
    val currencyCode: String
)