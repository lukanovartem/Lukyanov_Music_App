package com.example.lukyanovmusicapp.auth

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lukyanovmusicapp.R
import com.example.lukyanovmusicapp.navigation.Screen
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import com.example.lukyanovmusicapp.ui.theme.SatoshiFamily

@Composable
fun SignUpScreen(navHostController: NavHostController, viewModel: SignUpViewModel) {
    var emailState by remember{mutableStateOf("")}
    var passState by remember{mutableStateOf("")}
    var confirmPassState by remember{mutableStateOf("")}

    var signUpState = viewModel.signUpState.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign Up",
            fontFamily = SatoshiFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.ssp
        )

        Spacer(modifier = Modifier.height(25.sdp))

        /*TextField(
            value = "Full Name",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(0.70f)
                .height(50.sdp)
                .border(
                    width = 1.sdp,
                    color = colorResource(R.color.inactive_player_line),
                    shape = RoundedCornerShape(20.sdp)
                )
                .clip(RoundedCornerShape(15.sdp)),
            placeholder = { Text("Full Name", fontSize = 11.ssp) },
            singleLine = true,
            enabled = false,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )*/

        Spacer(modifier = Modifier.height(10.sdp))

        TextField(
            value = emailState,
            onValueChange = {emailState = it},
            modifier = Modifier
                .fillMaxWidth(0.70f)
                .height(50.sdp)
                .border(
                    width = 1.sdp,
                    color = colorResource(R.color.inactive_player_line),
                    shape = RoundedCornerShape(20.sdp)
                )
                .clip(RoundedCornerShape(15.sdp)),
            placeholder = { Text("Enter Email", fontSize = 11.ssp) },
            singleLine = true,
            enabled = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(10.sdp))

        TextField(
            value = passState,
            onValueChange = {
                passState = it
            },
            modifier = Modifier
                .fillMaxWidth(0.70f)
                .height(50.sdp)
                .border(
                    width = 1.sdp,
                    color = colorResource(R.color.inactive_player_line),
                    shape = RoundedCornerShape(20.sdp)
                )
                .clip(RoundedCornerShape(15.sdp)),
            placeholder = { Text("Enter Password", fontSize = 11.ssp) },
            singleLine = true,
            enabled = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(10.sdp))

        TextField(
            value = confirmPassState,
            onValueChange = {
                confirmPassState = it
            },
            modifier = Modifier
                .fillMaxWidth(0.70f)
                .height(50.sdp)
                .border(
                    width = 1.sdp,
                    color = colorResource(R.color.inactive_player_line),
                    shape = RoundedCornerShape(20.sdp)
                )
                .clip(RoundedCornerShape(15.sdp)),
            placeholder = { Text("Confirm Password", fontSize = 11.ssp) },
            singleLine = true,
            enabled = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )

        when (signUpState) {
            AuthResult.Loading -> {
                Spacer(modifier = Modifier.height(15.sdp))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(15.sdp))
            }
            is AuthResult.Error -> {
                Spacer(modifier = Modifier.height(15.sdp))
                Icon(
                    imageVector = Icons.Default.Info,
                    tint = Color.Red,
                    contentDescription = "error"
                )
                Spacer(modifier = Modifier.height(15.sdp))
            }
            AuthResult.Success -> {
                Spacer(modifier = Modifier.height(15.sdp))
                Icon(
                    imageVector = Icons.Default.Done,
                    tint = Color.Green,
                    contentDescription = "error"
                )
                Spacer(modifier = Modifier.height(15.sdp))
            }
            else -> {
                Spacer(modifier = Modifier.height(35.sdp))
            }
        }

        Button(
            modifier = Modifier
                .width(250.sdp)
                .height(55.sdp)
                .clip(RoundedCornerShape(30)),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.logo),
            ),
            onClick = {

                if (emailState.isNotBlank() && passState.isNotBlank() && confirmPassState.isNotBlank() && passState == confirmPassState) {
                    viewModel.signUpState.value = AuthResult.Loading
                    viewModel.createUser(emailState, passState)
                }
            }
        ) {
            Text(
                text = "Зареєструватися",
                fontFamily = SatoshiFamily,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 12.ssp
            )
        }

        Spacer(modifier = Modifier.height(25.sdp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.LightGray
            )
            Text(
                text = "Або",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.Gray
            )
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(5.sdp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Do you have an account?",
                fontFamily = SatoshiFamily,
                color = colorResource(R.color.active_player_line),
                fontWeight = FontWeight.Medium,
                fontSize = 11.ssp
            )
            Spacer(modifier = Modifier.width(10.sdp))
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(30))
                    .clickable {
                        navHostController.navigate(Screen.LoginScreen.route)
                    },
                text = "Sign In",
                fontFamily = SatoshiFamily,
                color = colorResource(R.color.blue_link),
                fontWeight = FontWeight.Medium,
                fontSize = 11.ssp
            )
        }
    }
}
