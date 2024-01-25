package com.example.reader.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.reader.components.FABContent
import com.example.reader.components.ListCard
import com.example.reader.components.ReaderAppBar
import com.example.reader.components.TitleSection
import com.example.reader.model.MBook
import com.example.reader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {

    Scaffold(topBar = {
        Surface(
            modifier = Modifier
                .padding(10.dp)
                .shadow(elevation = 10.dp)
                .clip(shape = RoundedCornerShape(15.dp)),
        ) {
            ReaderAppBar(title = "Reader", navController = navController)
        }
    }, floatingActionButton = {
        FABContent {
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    },
        modifier = Modifier
            .shadow(elevation = 5.dp)
    ) {
        Surface(modifier = Modifier
            .padding(it)
            .verticalScroll(rememberScrollState())
        ) {
            HomeContent(navController = navController)
        }

    }
}

@Preview
@Composable
fun HomeContent(navController: NavController = NavController(LocalContext.current)) {

    val listOfBooks = listOf(
        MBook(id = "kvbvkjn",title = "hello again", author = "alex", notes = "babu"),
        MBook(id = "kvbvkjn",title = "hello", author = "alex", notes = "babu"),
        MBook(id = "kvbvkjn",title = "hello again", author = "alex", notes = "babu"),
        MBook(id = "kvbvkjn",title = "hello again", author = "alex", notes = "babu"),
        MBook(id = "kvbvkjn",title = "hello again", author = "alex", notes = "babu"),
        MBook(id = "kvbvkjn",title = "hello again", author = "alex", notes = "babu"),
        MBook(id = "kvbvkjn",title = "hello again", author = "alex", notes = "babu"),
    )

    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUser = if (!email.isNullOrEmpty()) {
        FirebaseAuth.getInstance().currentUser?.email?.substringBefore("@")
    } else {
        "N/A"
    }

    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleSection(label = "Your Reading \n" + "activity right now")
            Spacer(modifier = Modifier.fillMaxWidth(0.8f))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle, contentDescription = "profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.StatsScreen.name)
                        }
                        .size(40.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = currentUser.toString(),
                    modifier = Modifier.padding(1.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()
            }
        }
        ReadingRightNowArea(books = listOf(), navController = navController)
        TitleSection(modifier = Modifier.padding(start = 10.dp), label = "Reading List...")

        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {

    HorizontalScrollableComponent(listOfBooks){
        Log.d("book", it)

    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPressed: (String) -> Unit) {

    val scrollState = rememberScrollState()

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ){
        for (book in listOfBooks){
            ListCard(book = book){
                onCardPressed(it)
            }
        }
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
    ListCard()
}


