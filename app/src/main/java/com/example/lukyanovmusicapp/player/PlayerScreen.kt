package com.example.lukyanovmusicapp.player


import androidx.compose.foundation.Image
import androidx.compose.foundation.background

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.lukyanovmusicapp.R
import com.example.lukyanovmusicapp.ui.theme.ArialFamily
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun PlayerView(
    navController: NavController,
    viewModel: PlayerViewModel,
) {
    val isPlaying by viewModel.isPlaying
    val position by viewModel.position
    val duration by viewModel.duration

    var sliderPosition by remember { mutableStateOf(0f) }
    var isSeeking by remember { mutableStateOf(false) }

    LaunchedEffect(position, isSeeking) {
        if (!isSeeking) {
            sliderPosition = position.toFloat()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = 90.sdp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Box(contentAlignment = Alignment.Center,modifier = Modifier.width(230.sdp).height(230.sdp).clip(shape = RoundedCornerShape(20)).background(colorResource(R.color.bottom_nav_item))) {
            if (viewModel.trackToPlay.value.id == 0L) {
                Row {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = colorResource(R.color.logo))
                    Spacer(modifier = Modifier.width(10.sdp))
                    Text(
                        modifier = Modifier.width(150.sdp),
                        text = "Ви ще не обрали трек, послухайте заглушку :)",
                        fontFamily = ArialFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.ssp,
                        color = Color.Black
                    )
                }
            } else {
                Image(modifier = Modifier.width(230.sdp).height(230.sdp),painter = rememberAsyncImagePainter(viewModel.trackToPlay.value.album.cover_big),contentDescription = null)
            }
        }

        Row (modifier = Modifier.fillMaxWidth(0.9f).padding(top = 25.sdp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column (modifier = Modifier) {
                Text(
                    modifier = Modifier,
                    text = viewModel.trackToPlay.value.title,
                    fontFamily = ArialFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.ssp,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier,
                    text = viewModel.trackToPlay.value.artist.name,
                    fontFamily = ArialFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.ssp,
                    color = Color.Black
                )
            }

        }
        Spacer(modifier = Modifier.height(30.sdp))
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                isSeeking = true
            },
            onValueChangeFinished = {
                isSeeking = false
                viewModel.seekTo(sliderPosition.toLong())
            },
            valueRange = 0f..(duration.toFloat().coerceAtLeast(1f)),
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = SliderDefaults.colors(
                thumbColor = colorResource(R.color.player_circle), // точка ползунка начала
                activeTrackColor = colorResource(R.color.active_player_line),
                inactiveTrackColor = colorResource(R.color.inactive_player_line)
            )
        )


        Row (modifier = Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${formatTime(position)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${formatTime(duration)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }


        Spacer(modifier = Modifier.height(32.dp))

        IconButton (modifier = Modifier.size(50.sdp), colors = IconButtonDefaults.iconButtonColors(
            containerColor = colorResource(R.color.logo)
        ),onClick = { viewModel.togglePlayPause() }) {
            Icon(painter = painterResource(
                if (isPlaying) {
                    R.drawable.baseline_pause_24
                } else {
                    R.drawable.outline_play_arrow_24
                }
            ),
                tint = Color.White, contentDescription = null)
        }
    }
}

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}
