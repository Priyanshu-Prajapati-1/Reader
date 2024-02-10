package com.example.reader.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.reader.components.ReaderAppBar
import com.example.reader.components.RoundedButton
import com.example.reader.components.ShowToast
import com.example.reader.data.Resource
import com.example.reader.model.BookModel.Item
import com.example.reader.model.MBook
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .padding(10.dp)
                    .height(36.dp)
                    .shadow(elevation = 10.dp)
                    .clip(shape = RoundedCornerShape(15.dp)),
            ) {
                ReaderAppBar(
                    title = "Book Details",
                    icon = Icons.Default.ArrowBack,
                    showProfile = false,
                    navController = navController
                ) {
                    navController.popBackStack()
                }
            }
        }
    ) {

        BackHandler {
            navController.popBackStack()
        }

        val context = LocalContext.current
        val ifBookExists = remember {
            mutableStateOf(false)
        }

        if (ifBookExists.value) {
            ShowToast("Book already exists", context)
        }

        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                        value = viewModel.getBookInfo(bookId = bookId)
                    }.value

                    val bookData = bookInfo.data?.volumeInfo

                    val imageUrl = bookData?.imageLinks?.smallThumbnail
                    val googleBookId = bookInfo.data?.id


                    Spacer(modifier = Modifier.height(40.dp))

                    if (bookData == null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp, vertical = 50.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Loading...")
                            Spacer(modifier = Modifier.height(5.dp))
                            LinearProgressIndicator()
                        }

                    } else {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SubcomposeAsyncImage(
                                model = imageUrl,
                                contentDescription = "image url",
                                modifier = Modifier
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .height(100.dp)
                                            .width(90.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(35.dp)
                                        )
                                    }
                                },
                                onError = {}
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    text = bookData.title,
                                    style = TextStyle(
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.W400

                                    )
                                )
                                Text(
                                    text = "Author: " + bookData.authors.toString(),
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.W400

                                    )
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                bookData.subtitle?.let { subtitle ->
                                    Text(
                                        text = subtitle,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W400

                                        )
                                    )
                                }

                            }

                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                            ) {
                                for (category in bookData.categories) {
                                    Text(
                                        text = ": $category",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.W400,
                                            fontStyle = FontStyle.Italic
                                        )
                                    )

                                }
                            }

                            Card {
                                Column(
                                    modifier = Modifier
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Publisher: " + bookData.publisher,
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.W400,
                                        )
                                    )
                                    Text(
                                        text = "Published Date: " + bookData.publishedDate,
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.W400,
                                        )
                                    )
                                    Text(
                                        text = "Pages: " + bookData.pageCount.toString(),
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.W400,
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            Column(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .fillMaxWidth()
                                    .border(
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.onTertiaryContainer
                                        ), shape = RoundedCornerShape(3.dp)
                                    ),
                                horizontalAlignment = Alignment.Start
                            ) {

                                Text(
                                    text = "Description",
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp),
                                    fontSize = 20.sp
                                )
                                Row(
                                    modifier = Modifier
                                        .height(1.dp)
                                        .fillMaxWidth()
                                        .background(color = MaterialTheme.colorScheme.onBackground)
                                ) {}

                                val contentDescription = HtmlCompat.fromHtml(
                                    bookData.description,
                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                ).toString()
                                Text(
                                    text = contentDescription,
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp, vertical = 5.dp)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            RoundedButton(label = "Save", ifLoadingProcess = true) {
                                val book = MBook(
                                    title = bookData.title,
                                    author = bookData.authors.toString(),
                                    description = bookData.description,
                                    categories = bookData.categories.toString(),
                                    notes = "",
                                    photoUrl = bookData.imageLinks.thumbnail,
                                    publishedDate = bookData.publishedDate,
                                    rating = 0.0,
                                    pageCount = bookData.pageCount.toString(),
                                    googleBookId = googleBookId,
                                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                                )
                                saveToFirebase(
                                    book = book,
                                    navController = navController,
                                    ifBookExist = ifBookExists
                                )
                            }
                            Spacer(modifier = Modifier.width(55.dp))
                            RoundedButton(label = "Cancel") {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun saveToFirebase(book: MBook, navController: NavController, ifBookExist: MutableState<Boolean>) {

    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    // check if book already available
    val bookCollection = Firebase.firestore
    val booksCollectionRef = bookCollection.collection("books")
    val query = booksCollectionRef.whereEqualTo("google_book_id", book.googleBookId).whereEqualTo("user_id", book.userId)

    query.get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                ifBookExist.value = true
                navController.popBackStack()
            } else {
                ifBookExist.value = false
                if (book.toString().isNotEmpty() && querySnapshot.isEmpty) {
                    dbCollection.add(book)
                        .addOnSuccessListener { documentReference ->
                            val docId = documentReference.id
                            dbCollection.document(docId)
                                .update(hashMapOf("id" to docId) as Map<String, Any>)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        navController.popBackStack()
                                    }
                                }
                                .addOnFailureListener {
                                    Log.d("error", "Error updating : $it")
                                }
                        }
                }
            }
        }
}
