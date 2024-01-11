package com.example.reader.navigation

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    HomeScreen,
    SearchScreen,
    DetailsScreen,
    UpdateScreen,
    StatsScreen,
    CreateAccountScreen;

    companion object{
        fun fromRoute(route: String?): ReaderScreens
        = when(route?.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            DetailsScreen.name -> DetailsScreen
            UpdateScreen.name -> UpdateScreen
            StatsScreen.name -> StatsScreen
            CreateAccountScreen.name -> CreateAccountScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}