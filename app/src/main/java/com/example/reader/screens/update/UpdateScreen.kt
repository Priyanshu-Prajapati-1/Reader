package com.example.reader.screens.update

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.reader.R
import com.example.reader.components.InputField
import com.example.reader.components.IsLoading
import com.example.reader.components.RatingBar
import com.example.reader.components.ReaderAppBar
import com.example.reader.components.RoundedButton
import com.example.reader.components.showToast
import com.example.reader.data.DataOrException
import com.example.reader.model.MBook
import com.example.reader.navigation.ReaderScreens
import com.example.reader.screens.home.HomeScreenViewModel
import com.example.reader.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState",
    "ProduceStateDoesNotAssignValue"
)
@Composable
fun UpdateScreen(
    navController: NavController,
    bookItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
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
                    title = "Update Book",
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
        Surface(
            modifier = Modifier
                .padding(it)
        ) {
            val bookInfo = produceState<DataOrException<List<MBook>, Boolean, Exception>>(
                initialValue = DataOrException(
                    data = emptyList(),
                    true,
                    Exception("")
                )
            ) {
                value = viewModel.data.value
            }.value

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    //Log.d("data", "BookUpdated : ${viewModel.data.value.data.toString()}")
                    if (bookInfo.loading == true) {
                        IsLoading(isCircular = false)
                        bookInfo.loading = false
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shadowElevation = 5.dp,
                        ) {
                            ShowUpdateBook(
                                bookInfo = viewModel.data.value,
                                bookItemId = bookItemId,
                                navController = navController
                            )
                        }

                        ShowSimpleForm(book = viewModel.data.value.data?.first { mBook ->
                            mBook.googleBookId == bookItemId
                        }!!, navController)

                       /* viewModel.data.value.data?.firstOrNull { mBook -> mBook.googleBookId == bookItemId }
                            ?.let { book ->
                                ShowSimpleForm(book = book, navController = navController)
                            }*/

                    }
                }
            }
        }
        BackHandler {
            navController.popBackStack()
        }
    }
}

@Composable
fun ShowSimpleForm(book: MBook, navController: NavController) {

    var notesText by rememberSaveable {
        mutableStateOf("")
    }
    var isStartedReading by rememberSaveable {
        mutableStateOf(false)
    }
    var isFinishedReading by rememberSaveable {
        mutableStateOf(false)
    }

    var ratingValue by rememberSaveable {
        mutableIntStateOf(0)
    }

    SimpleForm(
        defaultValue = book.notes.toString().ifEmpty { "No thoughts available :( " }
    ) { notes ->
        notesText = notes
    }

    Row(
        modifier = Modifier
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TextButton(
            onClick = {
                isStartedReading = true
            },
            enabled = book.startReading == null
        ) {
            if (book.startReading == null) {
                if (!isStartedReading) {
                    Text(
                        text = "Start Reading",
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = "Started Reading!",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                }
            } else {
                Text(
                    text = "Started on : ${formatDate(book.startReading!!)}",
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(7.dp))
        TextButton(
            onClick = {
                isFinishedReading = true
            },
            enabled = book.finishedReading == null
        ) {
            if (book.finishedReading == null) {
                if (!isFinishedReading) {
                    Text(
                        text = "Mark as Read",
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = "Finished Reading",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                }
            } else {
                Text(
                    text = "Finished on: ${formatDate(book.finishedReading!!)}",
                    fontSize = 12.sp
                )
            }
        }
    }
    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))

    book.rating?.toInt().let {
        RatingBar(rating = it!!) { rating ->
            ratingValue = rating
            //Log.d("Rating", "Rating value: $rating")
        }
    }

    Spacer(modifier = Modifier.padding(15.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val ifUpdating = remember {
            mutableStateOf(false)
        }

        val changeNotes = book.notes != notesText
        val changeRating = book.rating?.toInt() != ratingValue
        val isFinishedTimeStamp =
            if (isFinishedReading) Timestamp.now() else book.finishedReading
        val isStartedReadingTimestamp =
            if (isStartedReading) Timestamp.now() else book.startReading

        val bookUpdate =
            changeNotes || changeRating || isStartedReading || isFinishedReading

        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedReadingTimestamp,
            "rating" to ratingValue,
            "notes" to notesText
        ).toMap()

        RoundedButton(
            label = "Update",
            radius = 35,
            ifLoadingProcess = ifUpdating.value
        ) {
            if (bookUpdate) {
                ifUpdating.value = true
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener { _ ->
                        ifUpdating.value = false
                        //Log.d("task result", "result :$task")
                        navController.navigate(ReaderScreens.HomeScreen.name) {
                            popUpTo(0)
                        }
                    }
                    .addOnFailureListener {
                        ifUpdating.value = false
                        //Log.d("error in update", "Error : $it")
                    }
            } else {
                ifUpdating.value = true
                Thread.sleep(200)
                navController.popBackStack()
            }
        }

        val openDialogBox = remember {
            mutableStateOf(false)
        }
        val ifDeleteInProcess = remember {
            mutableStateOf(false)
        }

        val title: String =
            stringResource(id = R.string.sure) + "\n" + stringResource(id = R.string.action)

        val context = LocalContext.current

        if (openDialogBox.value) {
            ShowAlertDialogBox(message = title, openDialogBox, ifDeleteInProcess) {
                FirebaseFirestore.getInstance().collection("books")
                    .document(book.id!!)
                    .delete()
                    .addOnCompleteListener {
                        openDialogBox.value = false
                        navController.navigate(ReaderScreens.HomeScreen.name) {
                            popUpTo(0)
                        }
                    }
                    .addOnFailureListener {
                        openDialogBox.value = false
                        showToast(message = "Unable to delete\n Try Again", context = context)
                    }
            }
        }

        RoundedButton(
            label = "Delete",
            radius = 35
        ) {
            openDialogBox.value = true
        }
    }
    Spacer(modifier = Modifier.height(100.dp))
}

@Composable
fun ShowAlertDialogBox(
    message: String,
    openDialog: MutableState<Boolean>,
    ifDeleteInProcess: MutableState<Boolean>,
    onPressedYes: () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            backgroundColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(15.dp),
            onDismissRequest = {
                openDialog.value = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            text = {
                Text(
                    text = "Delete Book",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.error,
                )
            },
            title = {
                Text(
                    text = message,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            buttons = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    if (ifDeleteInProcess.value) {
                        Box(
                            modifier = Modifier.height(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LinearProgressIndicator()
                        }
                    }
                    Row(
                        modifier = Modifier.padding(6.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = {
                            ifDeleteInProcess.value = true
                            onPressedYes.invoke()
                        }) {
                            Text(text = "Yes")
                        }
                        Spacer(modifier = Modifier.width(130.dp))
                        TextButton(onClick = {
                            openDialog.value = false
                        }) {
                            Text(text = "No")
                        }
                    }
                }
            })
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun SimpleForm(
    defaultValue: String = "Great Book!",
    onSearch: (String) -> Unit
) {
    Column {
        val textFieldValue = rememberSaveable {
            mutableStateOf(defaultValue)
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val isValid = remember(textFieldValue.value) {
            textFieldValue.value.trim().isNotEmpty()
        }

        InputField(
            modifier = Modifier
                .padding(3.dp)
                .height(170.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            valueState = textFieldValue,
            labelId = "Enter Your Thoughts",
            enabled = true,
            isSingleLine = false,
            onAction = KeyboardActions {
                if (!isValid) return@KeyboardActions
                // onSearch(textFieldValue.value.trim())
                keyboardController?.hide()
            }){
            onSearch(textFieldValue.value.trim())
        }
    }
}

@Composable
fun ShowUpdateBook(
    bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
    bookItemId: String,
    navController: NavController
) {

    if (bookInfo.data != null && bookInfo.data!!.isNotEmpty()) {
        val book = bookInfo.data!!.firstOrNull { mBook ->
            mBook.googleBookId == bookItemId
        }
        if (book != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                CardListItem(book = book, onPressDetails = {
                    navController.navigate(ReaderScreens.DetailsScreen.name + "/${bookItemId}")
                })
            }
        } else {
            Box(
                Modifier.fillMaxSize()
            ) {
                Text("Book not found")
            }
        }
    } else {
        Box(
            Modifier.fillMaxSize()
        ) {
            Text("No book data available")
        }
    }

}

@Composable
fun CardListItem(book: MBook, onPressDetails: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(
                indication = null, // Disable the ripple effect
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onPressDetails()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            SubcomposeAsyncImage(
                model = book.photoUrl,
                contentDescription = "image url",
                modifier = Modifier
                    .height(120.dp)
                    .clip(RoundedCornerShape(5.dp)),
                loading = {
                    IsLoading(isCircular = true)
                },
                onError = {}
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .padding(4.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = book.title.toString(), overflow = TextOverflow.Clip,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                )
                Text(
                    text = "Author: ${book.author.toString()}", overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.W400
                    )
                )
                Text(
                    text = "Published date: ${book.publishedDate.toString()}",
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic
                    ),
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                )
            }
        }
    }

}
