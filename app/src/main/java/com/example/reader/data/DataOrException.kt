package com.example.reader.data

import androidx.compose.runtime.Immutable

@Immutable
data class DataOrException<T, Boolean, E : Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var error: E? = null
)
