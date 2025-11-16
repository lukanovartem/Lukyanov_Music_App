package com.example.lukyanovmusicapp.navigation

import androidx.annotation.DrawableRes
import com.example.lukyanovmusicapp.R

sealed class Screen(
    val title: String,
    val route: String,
    @DrawableRes val icon: Int = 0
    )
{

    object SignUpScreen: Screen(
        title = "Sign Up",
        route = "signup"
    )

    object LoginScreen: Screen(
        title = "Login",
        route = "login"
    )

    object HomeScreen: Screen(
        title = "Home",
        route = "home",
        icon = R.drawable.home_2
    )

    object PlaylistScreen: Screen(
        title = "Playlist",
        route = "playlist"
    )
    object UserPlaylistScreen: Screen(
        title = "User Playlist",
        route = "user_playlist"
    )


    object PlayerScreen: Screen(
        title = "Player",
        route = "player",
        icon = R.drawable.compass
    )

    object LikedScreen: Screen(
        title = "Liked",
        route = "liked",
        icon = R.drawable.heart_1
    )

    object OfflineScreen: Screen(
        title = "Offline",
        route = "offline_screen",
        icon = R.drawable.download
    )

}

val bottomNavigationScreens = listOf(
    Screen.HomeScreen,
    Screen.PlayerScreen,
    Screen.LikedScreen,
    Screen.OfflineScreen
)