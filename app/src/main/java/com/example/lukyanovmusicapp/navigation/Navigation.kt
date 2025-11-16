package com.example.lukyanovmusicapp.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lukyanovmusicapp.auth.LoginScreen
import com.example.lukyanovmusicapp.auth.SignUpScreen
import com.google.firebase.auth.FirebaseAuth
import network.chaintech.sdpcomposemultiplatform.sdp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.example.lukyanovmusicapp.auth.AuthRepository
import com.example.lukyanovmusicapp.auth.SignInViewModel
import com.example.lukyanovmusicapp.auth.SignUpViewModel
import com.example.lukyanovmusicapp.firestore.UserPlaylistViewModel
import com.example.lukyanovmusicapp.home.HomeView
import com.example.lukyanovmusicapp.home.OfficialPlaylistsViewModel
import com.example.lukyanovmusicapp.liked.LikedScreen
import com.example.lukyanovmusicapp.liked.LikedViewModel
import com.example.lukyanovmusicapp.player.PlayerView
import com.example.lukyanovmusicapp.player.PlayerViewModel
import com.example.lukyanovmusicapp.playlist.PlayListView
import com.example.lukyanovmusicapp.playlist.UserPlayListView
import com.example.lukyanovmusicapp.room.OfflineListeningScreen
import com.example.lukyanovmusicapp.room.RoomViewModel

@Composable
fun Navigation (navController: NavHostController) {
    val authRepository = remember {AuthRepository()}
    val signUpViewModel: SignUpViewModel = viewModel()
    val signInViewModel: SignInViewModel = viewModel()
    val officialPlaylistsViewModel: OfficialPlaylistsViewModel = viewModel()
    val playerViewModel: PlayerViewModel = viewModel()
    val likedViewModel: LikedViewModel = viewModel ()
    val roomViewModel: RoomViewModel = viewModel ()
    val userPlaylistViewModel: UserPlaylistViewModel = viewModel()
    val currentUser by authRepository.currentUser

    NavHost(startDestination =
        if (currentUser == null) {
            Screen.LoginScreen.route
        } else {
            Screen.HomeScreen.route
        }, navController = navController) {

        composable(Screen.LoginScreen.route) {
            LoginScreen(navController, signInViewModel)
        }

        composable (Screen.SignUpScreen.route) {
            SignUpScreen(navController, signUpViewModel)
        }

        composable (Screen.HomeScreen.route) {
            HomeView(officialPlaylistsViewModel, navController, userPlaylistViewModel)
        }

        composable (Screen.PlaylistScreen.route) {
            PlayListView(navController = navController, officialPlaylistsViewModel, playerViewModel, likedViewModel, userPlaylistViewModel)
        }

        composable (Screen.PlayerScreen.route) {
            PlayerView(navController = navController, playerViewModel)
        }

        composable (Screen.LikedScreen.route) {
            LikedScreen(navController = navController,  playerViewModel = playerViewModel, likedViewModel = likedViewModel, roomViewModel)
        }

        composable (Screen.OfflineScreen.route) {
            OfflineListeningScreen(navController = navController,  playerViewModel = playerViewModel, roomViewModel)
        }

        composable (Screen.UserPlaylistScreen.route) {
            UserPlayListView(navController = navController,  playerViewModel = playerViewModel, likedViewModel = likedViewModel, userPlaylistViewModel = userPlaylistViewModel)
        }

    }
}