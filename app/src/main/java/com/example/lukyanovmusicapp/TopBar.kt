package com.example.lukyanovmusicapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.lukyanovmusicapp.navigation.Screen
import network.chaintech.sdpcomposemultiplatform.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    if (!currentRoute.equals(Screen.SignUpScreen.route) && !currentRoute.equals(Screen.LoginScreen.route) && !currentRoute.equals(Screen.HomeScreen.route)) {
        TopAppBar(title = {}, navigationIcon = {
            IconButton(modifier = Modifier.padding(start = 10.sdp),onClick = {
                navController.navigateUp()
            }) {
                Icon(modifier = Modifier, painter = painterResource(R.drawable.baseline_arrow_back_ios_24), contentDescription = null)
            }
        })
    }

}