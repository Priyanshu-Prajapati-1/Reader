package com.example.reader.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.example.reader.components.InputField
import com.example.reader.components.IsLoading
import com.example.reader.components.ReaderAppBar
import com.example.reader.model.BookModel.Item
import com.example.reader.navigation.ReaderScreens


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: BookSearchViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .padding(10.dp)
                    .shadow(elevation = 10.dp)
                    .clip(shape = RoundedCornerShape(15.dp)),
            ) {
                ReaderAppBar(
                    title = "Search",
                    icon = Icons.Default.ArrowBack,
                    showProfile = false,
                    navController = navController
                ) {
//                    navController.popBackStack()
                    navController.navigate(ReaderScreens.HomeScreen.name)
                }
            }
        }

    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
            // .verticalScroll(rememberScrollState())   // not use this , it gave you many errors related to recomposition
        ) {
            Column {
                SearchTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    viewModel = viewModel
                ) { searchQuery ->
                    viewModel.searchBooks(query = searchQuery)
                }

                Spacer(modifier = Modifier.height(5.dp))

                BookList(navController = navController)
            }
        }
    }
}

@Composable
fun BookList(navController: NavController, viewModel: BookSearchViewModel = hiltViewModel()) {

    val listOfBooks = viewModel.list

    if (viewModel.isLoading) {
        IsLoading(isCircular = false)
    } else {
        LazyColumn(   /// this gives error
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) {
            items(items = listOfBooks) { book: Item ->
                BookRow(book = book, navController = navController)
            }
        }
    }
}

@Composable
fun BookRow(book: Item, navController: NavController) {

    val imageUrl = book.volumeInfo.imageLinks.smallThumbnail.ifEmpty {
        // "http://books.google.com/books/content?id=ZthJlG4o-2wC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
        R.drawable.image
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable {
                navController.navigate(ReaderScreens.DetailsScreen.name + "/${book.id}")
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

            Column(
                modifier = Modifier.padding(vertical = 7.dp, horizontal = 5.dp)
            ) {
                Text(
                    text = book.volumeInfo.title, overflow = TextOverflow.Clip,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "Author: ${book.volumeInfo.authors}", overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Published date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic
                    ),
                    textDecoration = TextDecoration.Underline
                )
                Text(
                    text = "${book.volumeInfo.categories}", overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    viewModel: BookSearchViewModel,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column() {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = rememberSaveable(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(modifier = modifier,
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )

    }
}