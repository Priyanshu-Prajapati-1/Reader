package com.example.reader.model.BookModel

import androidx.compose.runtime.Immutable

@Immutable
data class PanelizationSummary(
    val containsEpubBubbles: Boolean,
    val containsImageBubbles: Boolean
)