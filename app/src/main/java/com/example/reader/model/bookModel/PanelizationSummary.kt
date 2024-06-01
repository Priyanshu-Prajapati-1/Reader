package com.example.reader.model.bookModel

import androidx.compose.runtime.Immutable

@Immutable
data class PanelizationSummary(
    val containsEpubBubbles: Boolean,
    val containsImageBubbles: Boolean
)