package com.example.lukyanovmusicapp.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.lukyanovmusicapp.R

import com.google.firebase.auth.FirebaseAuth
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun ProfileView(navController: NavHostController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userName by remember {mutableStateOf(currentUser?.displayName)}
    val userEmail by remember {mutableStateOf(currentUser?.email)}

    Column (modifier = Modifier.fillMaxSize().padding(top = 84.sdp).background(colorResource(R.color.light_bg)), horizontalAlignment = Alignment.CenterHorizontally){
        Column (modifier = Modifier.fillMaxWidth().height(350.sdp).clip(shape = RoundedCornerShape(bottomEnd = 55.sdp, bottomStart = 55.sdp)).background(colorResource(R.color.system_bg))
            .padding(top = 0.sdp), horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Профіль", fontSize = 14.ssp)
            Spacer(modifier = Modifier.height(30.sdp))
            Box(modifier = Modifier.size(120.sdp).clip(RoundedCornerShape(50)).background(colorResource(R.color.light_gray)), contentAlignment = Alignment.Center) {
                Image(modifier = Modifier.size(120.sdp), painter = painterResource(R.drawable.lukyanoff), contentDescription = null)
            }
            Spacer(modifier = Modifier.height(15.sdp))
            Text(text = userName.toString(), fontSize = 14.ssp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(5.sdp))
            Text(text = userEmail.toString(), fontSize = 14.ssp, fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier.height(15.sdp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                FounderButton()
                Spacer(modifier = Modifier.width(15.sdp))
                IconButton(onClick = {

                }) {
                    Icon(painter = painterResource(R.drawable.baseline_logout_24), contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun FounderButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .border(1.sdp, Color.Black, RoundedCornerShape(50))
            .background(Color.White)
            .padding(horizontal = 14.sdp, vertical = 8.sdp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Засновник",
                fontStyle = FontStyle.Italic,
                fontSize = 10.ssp,
                color = Color.Black
            )
        }
    }
}
