package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable

@Immutable
data class Offer(
    val finskyOfferType: Int,
    val listPrice: ListPriceX,
    val retailPrice: RetailPrice
)