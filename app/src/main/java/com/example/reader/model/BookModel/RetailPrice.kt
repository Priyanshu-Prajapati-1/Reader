package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable

@Immutable
data class RetailPrice(
    val amountInMicros: Long,
    val currencyCode: String
)