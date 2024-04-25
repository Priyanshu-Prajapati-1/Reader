package com.example.reader.model

import androidx.compose.runtime.Immutable
import  com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

@Immutable
data class MBook(

    @Exclude
    var id: String? = null,

    var title: String? = null,
    var author: String? = null,
    var notes: String? = null,

    @get:PropertyName(value = "book_photo_url")
    @set:PropertyName(value = "book_photo_url")
    var photoUrl: String? = null,

    var categories: String? = null,

    @get:PropertyName(value = "published_date")
    @set:PropertyName(value = "published_date")
    var publishedDate: String? = null,

    var rating: Double? = null,
    var description: String? = null,

    @get:PropertyName(value = "page_count")
    @set:PropertyName(value = "page_count")
    var pageCount: String? = null,

    @get:PropertyName(value = "started_reading_at")
    @set:PropertyName(value = "started_reading_at")
    var startReading: Timestamp? = null,

    @get:PropertyName(value = "finished_reading_at")
    @set:PropertyName(value = "finished_reading_at")
    var finishedReading: Timestamp? = null,

    @get:PropertyName(value = "user_id")
    @set:PropertyName(value = "user_id")
    var userId: String? = null,

    @get:PropertyName(value = "google_book_id")
    @set:PropertyName(value = "google_book_id")
    var googleBookId: String? = null,
)
