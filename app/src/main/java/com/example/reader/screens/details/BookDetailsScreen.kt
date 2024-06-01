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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.reader.R
import com.example.reader.components.ReaderAppBar
import com.example.reader.components.RoundedButton
import com.example.reader.components.showToast
import com.example.reader.data.Resource
import com.example.reader.model.MBook
import com.example.reader.model.bookModel.Item
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel(),
) {

    Scaffold(
        modifier = Modifier
            .systemBarsPadding(),
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
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
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

        val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
            value = viewModel.getBookInfo(bookId = bookId)
        }.value

        val bookData = bookInfo.data?.volumeInfo
//        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTudN8Com396LDgdLI1R57J7754r1KnnFWHAA&s"
        val imageUrl =
            if (bookData?.imageLinks?.smallThumbnail != null) bookData.imageLinks.smallThumbnail else R.drawable.book_image
        val googleBookId = bookInfo.data?.id
        val title = if (bookData?.title != null) bookData.title else ""
        val author = if (bookData?.authors != null) bookData.authors.toString() else ""
        val subtitle = if (bookData?.subtitle != null) bookData.subtitle else ""
        val categories = if (bookData?.categories != null) bookData.categories else listOf("")
        val publisher = if (bookData?.publisher != null) bookData.publisher else ""
        val publishedDate = if (bookData?.publishedDate != null) bookData.publishedDate else ""
        val pageCount = if (bookData?.pageCount != null) bookData.pageCount.toString() else ""
        val description = if (bookData?.description != null) bookData.description else ""
        val photoUrl =if (bookData?.imageLinks?.smallThumbnail != null) bookData.imageLinks.smallThumbnail else "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTudN8Com396LDgdLI1R57J7754r1KnnFWHAA&s"

        val book = MBook(
            title = title,
            author = author,
            description = description,
            categories = categories.toString(),
            notes = "",
            photoUrl = photoUrl,
            publishedDate = publishedDate,
            rating = 0.0,
            pageCount = pageCount,
            googleBookId = googleBookId,
            userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        )

        val ifLoadingProcess = remember {
            mutableStateOf(true)
        }

        if (ifBookExists.value) {
            showToast("book already exists", context = context)
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
                                    text = title,
                                    style = TextStyle(
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.W400

                                    )
                                )
                                Text(
                                    text = author,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.W400

                                    )
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = subtitle,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W400

                                    )
                                )

                            }

                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                            ) {
                                for (category in categories) {
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
                                        text = "Publisher: $publisher",
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.W400,
                                        )
                                    )
                                    Text(
                                        text = "Published Date: $publishedDate",
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.W400,
                                        )
                                    )
                                    Text(
                                        text = "Pages: $pageCount",
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
                                    bookData.description ?: "No Description",
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
                            RoundedButton(
                                label = "Save",
                                ifLoadingProcess = ifLoadingProcess.value
                            ) {
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
    val query = booksCollectionRef
        .whereEqualTo("google_book_id", book.googleBookId)
        .whereEqualTo("user_id", book.userId)

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
