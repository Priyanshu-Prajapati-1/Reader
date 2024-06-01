package com.example.reader.model.bookModel

import androidx.compose.runtime.Immutable

@Immutable
data class RetailPriceX(
    val amount: Double,
    val currencyCode: String
)