package com.example.reader.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.reader.R
import com.example.reader.model.MBook
import com.example.reader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        text = "Reader",
        modifier = modifier.padding(bottom = 16.dp),
        color = Color.Red.copy(alpha = 0.5f),
        fontSize = 45.sp,
        style = TextStyle(
            fontWeight = FontWeight.Bold
        )
    )
}


@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = {
            Text(
                text = labelId,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
            .padding(bottom = 10.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    )
}

@Composable
fun TitleSection(modifier: Modifier = Modifier, label: String = "Title Section") {
    Surface(
        modifier = modifier
            .padding(start = 5.dp, top = 1.dp)
    ) {
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontStyle = FontStyle.Normal,
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderAppBar(
    title: String,
    userName: String? = null,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onProfileClicked: () -> Unit = {},
    onBackArrowClicked: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showProfile) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Icon",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onProfileClicked() }
                            .size(35.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
                    )
                }

                if (icon != null) {
                    Icon(
                        imageVector = icon, contentDescription = "icon",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                        modifier = Modifier.clickable {
                            onBackArrowClicked.invoke()
                        }
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title, fontSize = if (showProfile) 18.sp else 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                        lineHeight = 19.sp
                    )
                    (if (showProfile) 10.sp else null)?.let {
                        Text(
                            text = userName.toString(),
                            fontSize = it,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                            lineHeight = 11.sp
                        )
                    }
                }
            }
        },
        actions = {
            if (showProfile) {
                IconButton(onClick = {
                    FirebaseAuth.getInstance().signOut().run {
                        navController.navigate(ReaderScreens.LoginScreen.name) {
                            popUpTo(0)
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "logout icon",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                    )
                }
            }
        },
        navigationIcon = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(0.dp)
            .height(50.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .border(
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.50f)
                ),
                shape = RoundedCornerShape(15.dp)
            )
    )
}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap.invoke() },
        shape = RoundedCornerShape(50.dp),
        containerColor = MaterialTheme.colorScheme.onSecondary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a book",
            tint = MaterialTheme.colorScheme.secondary
        )
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

@Preview
@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 30,
    ifLoadingProcess: Boolean = false,
    onPress: () -> Unit = {}
) {

    val ifBookSave = rememberSaveable {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEndPercent = radius, topStartPercent = radius)),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Column(
            modifier = Modifier
                .width(85.dp)
                .heightIn(35.dp)
                .clickable {
                    ifBookSave.value = !ifBookSave.value
                    onPress.invoke()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (ifBookSave.value && ifLoadingProcess) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(25.dp)
                )
            } else {
                Text(
                    text = label,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
                        fontSize = 15.sp
                    )
                )
            }
        }
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

    Box(
        modifier = Modifier
            .padding(15.dp)
            .height(240.dp)
            .width(180.dp)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp,
                pressedElevation = 7.dp
            ),
            modifier = Modifier
                .height(240.dp)
                .width(180.dp)
                .clickable {
                    onPressDetails.invoke(book.googleBookId.toString())
                }
        ) {
            Column(
                modifier = Modifier
                    .width(screenWidth.dp - (10.dp * 2)),
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
                        model = if (book.photoUrl == null) R.drawable.book_image else book.photoUrl,
                        contentDescription = "Book image",
                        modifier = Modifier
                            .height(140.dp)
                            .width(100.dp)
                            .clip(RoundedCornerShape(bottomEnd = 15.dp)),
                        contentScale = ContentScale.Crop,
                        loading = {
                            IsLoading(isCircular = true, modifier = Modifier.size(35.dp))
                            // Show a loading indicator
                        },
                        onError = {}
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

                        book.rating?.let { BookRating(score = it) }
                    }
                }
                Text(
                    text = book.title.toString(),
                    modifier = Modifier
                        .padding(horizontal = 7.dp, vertical = 3.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                )

                Text(
                    text = book.author.toString(),
                    modifier = Modifier
                        .padding(horizontal = 7.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                )
            }

        }

        val isStartedReading = remember {
            mutableStateOf(false)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            isStartedReading.value = book.startReading != null

            RoundedButton(
                label = if (isStartedReading.value) "Reading" else "Not Started",
                radius = 20
            )
        }
    }
}

@Composable
fun IsLoading(
    isCircular: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    if (isCircular) {
        Box(
            modifier = modifier
                .height(100.dp)
                .width(90.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(35.dp)
            )
        }
    } else {
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
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }
    var selected by remember {
        mutableStateOf(false)
    }

    val size by animateDpAsState(
        targetValue = if (selected) 44.dp else 34.dp, spring(Spring.DampingRatioMediumBouncy),
        label = ""
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}

fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}