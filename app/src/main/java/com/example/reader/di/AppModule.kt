package com.example.reader.di

import com.example.reader.network.BooksApi
import com.example.reader.repository.BookRepository
import com.example.reader.repository.FireRepository
import com.example.reader.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFireBookRepository() =
        FireRepository(queryBook = FirebaseFirestore.getInstance().collection("books"))

    @Provides
    @Singleton
    fun provideBookRepository(api: BooksApi) = BookRepository(api)

    @Provides
    @Singleton
    fun provideBookApi(): BooksApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }
}