package com.example.reader.screens.stats

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.reader.R
import com.example.reader.components.IsLoading
import com.example.reader.components.ReaderAppBar
import com.example.reader.model.MBook
import com.example.reader.navigation.ReaderScreens
import com.example.reader.screens.home.HomeScreenViewModel
import com.example.reader.utils.formatDate
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {

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
                    title = "Book stats",
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    showProfile = false,
                    navController = navController,
                ) {
                    navController.navigate(ReaderScreens.HomeScreen.name) {
                        popUpTo(0)
                    }
                }
            }
        }
    ) {

        var books: List<MBook>

        val currentUser = FirebaseAuth.getInstance().currentUser

        val email = FirebaseAuth.getInstance().currentUser?.email
        val currentUserName = if (!email.isNullOrEmpty()) {
            FirebaseAuth.getInstance().currentUser?.email?.substringBefore("@")
        } else {
            "N/A"
        }

        Surface(
            modifier = Modifier
                .padding(paddingValues = it)
                .fillMaxSize()
        ) {
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            } else {
                emptyList()
            }

            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Hi $currentUserName",
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.inverseSurface
                    )
                )

                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp,
                    ),
                ) {

                    val readingBookList: List<MBook> =
                        if (!viewModel.data.value.data.isNullOrEmpty()) {
                            books.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                            }
                        } else {
                            emptyList()
                        }
                    Column(
                        modifier = Modifier
                            .padding(7.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Your Stats",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.inverseSurface
                            )
                        )
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline
                        )

                        val readingBooks = books.filter { mBook ->
                            (mBook.startReading != null && mBook.finishedReading == null)
                        }
                        Text(
                            text = "You're reading: ${readingBooks.size} Books",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )

                        Text(
                            text = "You've read: ${readingBookList.size} Books",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                }

                if (viewModel.data.value.loading == true) {
                    IsLoading(
                        isCircular = false
                    )
                } else {

                    val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        viewModel.data.value.data!!.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }
                    } else {
                        emptyList()
                    }

                    if (readBooks.isNotEmpty()) {
                        LazyColumn(   /// this gives error
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            items(items = readBooks) { book: MBook ->
                                BookRow(book = book, navController = navController)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Empty",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                    }
                }
            }
        }
        BackHandler {
            navController.navigate(ReaderScreens.HomeScreen.name) {
                popUpTo(0)
            }
        }
    }
}

@Composable
fun BookRow(book: MBook, navController: NavController) {

    val imageUrl = if (book.photoUrl != null) book.photoUrl else R.drawable.book_image

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable {
                navController.navigate(ReaderScreens.DetailsScreen.name + "/${book.googleBookId}")
            },
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {

            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "Book image",
                modifier = Modifier
                    .height(100.dp)
                    .clip(RoundedCornerShape(5.dp)),
                loading = {
                    IsLoading(isCircular = true)
                },
                onError = {}
            )
            Spacer(modifier = Modifier.width(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                if(book.rating!! > 3.0){
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        modifier = Modifier
                            .padding(5.dp)
                            .align(
                            Alignment.BottomEnd
                        ),
                        tint = Color.Yellow
                    )
                }

                Column(
                    modifier = Modifier.padding(vertical = 7.dp, horizontal = 5.dp)
                ) {
                    Text(
                        text = book.title.toString(), overflow = TextOverflow.Clip,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "Author: ${book.author.toString()}",
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Started: ${book.startReading?.let { formatDate(it) }}",
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic
                        ),
                        textDecoration = TextDecoration.Underline
                    )
                    Text(
                        text = "Finished: ${book.finishedReading?.let { formatDate(it) }}",
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}