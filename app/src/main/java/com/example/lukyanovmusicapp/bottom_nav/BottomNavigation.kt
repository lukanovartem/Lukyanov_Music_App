package com.example.lukyanovmusicapp.bottom_nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lukyanovmusicapp.navigation.bottomNavigationScreens
import androidx.compose.runtime.getValue
import com.example.lukyanovmusicapp.navigation.Screen

@Composable
fun BottomNavigation (navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination?.route
    if (!currentDestination.equals(Screen.LoginScreen.route) && !currentDestination.equals(Screen.SignUpScreen.route)) {
        NavigationBar {
            bottomNavigationScreens.forEach {
                NavigationBarItem(onClick = {
                    navController.navigate(it.route)
                }, selected = if (currentDestination == it.route) {
                    true
                } else {
                    false
                },
                    icon = {
                        Icon(painter = painterResource(it.icon), contentDescription = null)
                    },
                )
            }
        }
    }

}