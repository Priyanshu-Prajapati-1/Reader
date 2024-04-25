package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable

@Immutable
data class ListPriceX(
    val amountInMicros: Long,
    val currencyCode: String
)