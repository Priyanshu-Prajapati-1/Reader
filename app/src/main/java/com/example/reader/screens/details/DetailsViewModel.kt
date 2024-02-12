package com.example.reader.screens.details

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.reader.data.Resource
import com.example.reader.model.BookModel.Item
import com.example.reader.model.MBook
import com.example.reader.repository.BookRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {

    var ifLoading: Boolean = false

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return bookRepository.getBookInfo(bookId)
    }

    suspend fun getReadingDataForBook(
        googleBookId: String,
        userId: String,
        ifComplete: MutableState<Boolean>,
        ifStartReading: MutableState<Boolean>,
        ifBookInReadingList: MutableState<Boolean>
    ) {
        ifLoading = false

        val db = Firebase.firestore
        val bookCollectionRef = db.collection("books")

        try {
            val querySnapshot = bookCollectionRef
                .whereEqualTo("google_book_id", googleBookId)
                .whereEqualTo("user_id", userId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val documentSnapshot = querySnapshot.documents.first()
                val startReading = documentSnapshot.getTimestamp("started_reading_at")
                val finishedReading = documentSnapshot.getTimestamp("finished_reading_at")

                if (finishedReading != null) { // then user complete the book
                    ifComplete.value = true
                } else {
                    if (startReading != null) { // then user start reading book
                        ifStartReading.value = true
                    } else {
                        ifBookInReadingList.value = true
                    }
                }
            }
            ifLoading = true

        } catch (e: Exception) {
            Log.d("Error getting reading dates for book: ", e.toString())
        }
    }

    suspend fun getMBookByGoogleBookIdAndUserId(googleBookId: String, userId: String): MBook? {
        ifLoading = false
        val db = Firebase.firestore
        val booksCollectionRef = db.collection("books")

        try {
            val querySnapshot = booksCollectionRef
                .whereEqualTo("google_book_id", googleBookId)
                .whereEqualTo("user_id", userId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val documentSnapshot = querySnapshot.documents.first()
                return documentSnapshot.toObject(MBook::class.java)
            }
            ifLoading = true
        } catch (e: Exception) {
            // Handle any errors
            println("Error getting MBook: $e")
        }

        return null
    }


//    coroutineScope.launch {
//        val mBook: MBook? = googleBookId?.let { it1 ->
//            viewModel.getMBookByGoogleBookIdAndUserId(
//                it1,
//                FirebaseAuth.getInstance().currentUser?.uid.toString()
//            )
//        }
//
//        if(mBook?.finishedReading != null){
//            ifComplete.value = true
//            Log.d("finishedReading", mBook?.startReading.toString())
//            Log.d("finishedReading", mBook?.finishedReading.toString())
//
//
//            saveToFirebase(
//                book = book,
//                navController = navController,
//                ifBookExist = ifBookExists
//            )
//        }else{
//            if(mBook?.startReading != null){
//                ifStartReading.value = true
//                Log.d("startReading", mBook?.startReading.toString())
//                Log.d("startReading", mBook?.finishedReading.toString())
//
//
//            }else{
//                ifBookInReadingList.value = true
//                Log.d("mbook", mBook?.startReading.toString())
//                Log.d("mbook", mBook?.finishedReading.toString())
//            }
//        }
//
//        if(mBook?.userId == null && mBook?.id == null){
//            saveToFirebase(
//                book = book,
//                navController = navController,
//                ifBookExist = ifBookExists
//            )
//        }
//    }


}