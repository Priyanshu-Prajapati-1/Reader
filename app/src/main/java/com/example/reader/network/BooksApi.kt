package com.example.reader.network

import com.example.reader.model.bookModel.Book
import com.example.reader.model.bookModel.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {
    // https://www.googleapis.com/books/v1/volumes?q=android

    @GET(value = "volumes")
    suspend fun getAllBooks(
        @Query("q") query: String
    ): Book

    @GET(value = "volumes/{bookId}")
    suspend fun getBookInfo(
        @Path(value = "bookId") bookId: String
    ): Item
}