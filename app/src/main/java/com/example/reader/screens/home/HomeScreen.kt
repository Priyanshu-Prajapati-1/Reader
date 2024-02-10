package com.example.reader.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.reader.components.FABContent
import com.example.reader.components.IsLoading
import com.example.reader.components.ListCard
import com.example.reader.components.ReaderAppBar
import com.example.reader.components.TitleSection
import com.example.reader.model.MBook
import com.example.reader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

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
        Surface(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            HomeContent(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun HomeContent(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HomeScreenViewModel
) {

//    val listOfBooks = listOf(
//        MBook(id = "kvbvkjn", title = "hello again", author = "alex", notes = "babu"),
//        MBook(id = "kvbvkjn", title = "hello", author = "alex", notes = "babu"),
//        MBook(id = "kvbvkjn", title = "hello again", author = "alex", notes = "babu"),
//        MBook(id = "kvbvkjn", title = "hello again", author = "alex", notes = "babu"),
//    )

    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }
    }

    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty()) {
        FirebaseAuth.getInstance().currentUser?.email?.substringBefore("@")
    } else {
        "N/A"
    }

    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
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
                    text = currentUserName.toString(),
                    modifier = Modifier.padding(1.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()
            }
        }

        if(viewModel.data.value.loading==true) {
            IsLoading(
                isCircular = true, modifier = Modifier
                    .height(300.dp)
                    .width(550.dp)
            )
        }else{
            if (!listOfBooks.isNullOrEmpty()) {
                ReadingRightNowArea(listOfBooks = listOfBooks, navController = navController)
                TitleSection(modifier = Modifier.padding(start = 10.dp), label = "Reading List...")

                if (viewModel.data.value.loading == true) {
                    IsLoading(
                        isCircular = true, modifier = Modifier
                            .height(300.dp)
                            .width(550.dp)
                    )
                } else {
                    BookListArea(listOfBooks = listOfBooks, navController = navController)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "empty")
                }
            }
        }
    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {

    val addedBook = listOfBooks.filter { mBook ->
        mBook.startReading == null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(addedBook) {
        Log.d("book", it)
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun ReadingRightNowArea(listOfBooks: List<MBook>, navController: NavController) {

    // filter books by reading now
    val readingNowList = listOfBooks.filter { mBook ->
        mBook.startReading != null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(readingNowList) {
        Log.d("book", it)
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfBooks: List<MBook>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCardPressed: (String) -> Unit
) {

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ) {

        if (viewModel.data.value.loading == true) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(80.dp)
            )

        } else {
            if (listOfBooks.isEmpty()) {
                val configuration = LocalConfiguration.current

                val screenHeight = configuration.screenHeightDp.dp / 3
                val screenWidth = configuration.screenWidthDp.dp
                Column(
                    modifier = Modifier
                        .padding(0.dp)
                        .height(screenHeight)
                        .width(screenWidth),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No Books found. Add Book",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }
            } else {
                for (book in listOfBooks) {
                    ListCard(book = book) {
                        onCardPressed(it)
                    }
                }
            }
        }
    }
}