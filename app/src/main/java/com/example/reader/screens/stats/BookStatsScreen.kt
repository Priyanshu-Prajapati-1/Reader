package com.example.reader.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun StatsScreen(navController: NavController){

    Column {


        Text(text = "Stats Screen")
        SubcomposeAsyncImage(
            model = "https://wallpapers.com/images/hd/wallpaper-chain-iron-metal-links-blur-1wkvbpapxb6wafjx.webp",
            contentDescription = "Book image",
            modifier = Modifier
                .height(140.dp)
                .width(90.dp)
                .padding(4.dp),
            contentScale = ContentScale.Crop,
            loading = {
                // Show a loading indicator
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
            },
            onError = {
                // Show an error message
            }
        )

        Image(
            painter = rememberAsyncImagePainter(model = "https://wallpapers.com/images/hd/wallpaper-chain-iron-metal-links-blur-1wkvbpapxb6wafjx.webp"),
            contentDescription = "book image",
            modifier = Modifier
                .height(140.dp)
                .width(90.dp)
                .padding(4.dp)
        )
    }
}