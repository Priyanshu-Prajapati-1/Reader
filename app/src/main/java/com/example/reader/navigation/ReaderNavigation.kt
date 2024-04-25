package com.example.reader.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reader.screens.SplashScreen
import com.example.reader.screens.details.BookDetailsScreen
import com.example.reader.screens.home.HomeScreen
import com.example.reader.screens.home.HomeScreenViewModel
import com.example.reader.screens.login.LoginScreen
import com.example.reader.screens.search.BookSearchViewModel
import com.example.reader.screens.search.SearchScreen
import com.example.reader.screens.stats.StatsScreen
import com.example.reader.screens.update.UpdateScreen

const val time = 500

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ReaderScreens.SplashScreen.name,
        enterTransition = {
            fadeIn(animationSpec = tween(time)) +
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(time)
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(time)) +
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(time)
                    )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(time)) +
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(time)
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(time)) +
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(time)
                    )
        },
    ) {

        composable(ReaderScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        composable(ReaderScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(ReaderScreens.HomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }

        composable(ReaderScreens.StatsScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            StatsScreen(navController = navController, viewModel = homeViewModel)
        }

        composable(
            ReaderScreens.UpdateScreen.name + "/{bookItemId}",
            arguments = listOf(navArgument("bookItemId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("bookItemId").let {
                UpdateScreen(navController = navController, bookItemId = it.toString())
            }
        }


        composable(
            ReaderScreens.DetailsScreen.name + "/{bookId}",
            arguments = listOf(navArgument("bookId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }

        composable(ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<BookSearchViewModel>()
            SearchScreen(navController = navController, viewModel = searchViewModel)
        }
    }
}