package com.example.lukyanovmusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.lukyanovmusicapp.auth.LoginScreen
import com.example.lukyanovmusicapp.auth.SignUpScreen
import com.example.lukyanovmusicapp.bottom_nav.BottomNavigation
import com.example.lukyanovmusicapp.navigation.Navigation
import com.example.lukyanovmusicapp.ui.theme.LukyanovMusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LukyanovMusicAppTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {TopBar(navController)},bottomBar = {BottomNavigation(navController)}) { innerPadding ->
                    Navigation(navController)
                }
            }
        }
    }
}
