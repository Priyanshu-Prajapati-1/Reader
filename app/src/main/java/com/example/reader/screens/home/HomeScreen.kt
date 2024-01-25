package com.example.reader.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.reader.components.FABContent
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
        FABContent {}
    },
        modifier = Modifier
            .shadow(elevation = 5.dp)
    ) {
        Surface(modifier = Modifier.padding(it)) {
            HomeContent(navController = navController)
        }

    }
}

@Preview
@Composable
fun HomeContent(navController: NavController = NavController(LocalContext.current)) {

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
        ListCard()
    }
}

@Preview
@Composable
fun ListCard(
    book: MBook = MBook("asaf", "Running", "me and you", "hello world"),
    onPressDetails: (String) -> Unit = {}
) {

    val context = LocalContext.current
    val resources = context.resources

    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density

    val spacing = 10.dp

    Card(
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 7.dp
        ),
        modifier = Modifier
            .padding(15.dp)
            .height(240.dp)
            .width(180.dp)
            .clickable {
                onPressDetails.invoke(book.title.toString())
            }
    ) {
        Column(
            modifier = Modifier
                .width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                SubcomposeAsyncImage(
                    model = "http://books.google.com/books/content?id=ZthJlG4o-2wC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                    contentDescription = "Book image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(0.dp)
                        .clip(RoundedCornerShape(bottomEnd = 20.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box (
                            modifier = Modifier.size(35.dp),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(35.dp)
                            )
                        }
                        // Show a loading indicator
                    },
                    onError = {
                        // Show an error message
                    }
                )


                Column(
                    modifier = Modifier
                        .padding(top = 15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite icon",
                        modifier = Modifier
                            .padding(bottom = 1.dp)
                    )

                    BookRating(score = 3.5)
                }
            }
            Text(
                text = "Book title",
                modifier = Modifier
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            ) // ...

            Text(
                text = "Author",
                modifier = Modifier
                    .padding(horizontal = 7.dp, vertical = 2.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            RoundedButton(label = "Reading", radius = 60)
        }
    }
}

@Preview
@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEndPercent = radius, topStartPercent = radius)),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .heightIn(40.dp)
                .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun BookRating(score: Double = 4.5) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .width(40.dp)
            .padding(4.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(50.dp))
            .clickable { },
        shape = RoundedCornerShape(50.dp),
        color = MaterialTheme.colorScheme.onSecondary
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 7.dp, horizontal = 1.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.StarBorder, contentDescription = "Star icon",
                modifier = Modifier.padding(1.dp)
            )

            Text(
                text = score.toString(),
                style = TextStyle(
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Medium
                )
            )

        }

    }
}


@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {

}


