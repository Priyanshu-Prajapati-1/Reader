package com.example.reader.model.bookModel

import androidx.compose.runtime.Immutable

@Immutable
data class RetailPrice(
    val amountInMicros: Long,
    val currencyCode: String
)