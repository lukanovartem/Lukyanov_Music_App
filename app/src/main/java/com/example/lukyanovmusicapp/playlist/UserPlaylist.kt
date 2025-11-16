package com.example.lukyanovmusicapp.playlist


import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info

import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lukyanovmusicapp.ui.theme.SatoshiFamily
import com.example.lukyanovmusicapp.R
import com.example.lukyanovmusicapp.firestore.UserPlaylistViewModel
import com.example.lukyanovmusicapp.home.OfficialPlaylistTracksResult
import com.example.lukyanovmusicapp.home.OfficialPlaylistsViewModel
import com.example.lukyanovmusicapp.liked.LikedViewModel
import com.example.lukyanovmusicapp.navigation.Screen
import com.example.lukyanovmusicapp.player.PlayerViewModel
import com.example.lukyanovmusicapp.retrofit.Artist
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import com.example.lukyanovmusicapp.ui.theme.ArialFamily
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun UserPlayListView(navController: NavHostController,
                 playerViewModel: PlayerViewModel,
                 likedViewModel: LikedViewModel,
                     userPlaylistViewModel: UserPlaylistViewModel)
{
    var showAlertDialog by remember { mutableStateOf(false) }
    val playlistTracksResult by userPlaylistViewModel.playlistTracks.collectAsState()
    val selectedPlaylist by userPlaylistViewModel.selectedPlaylist.collectAsState()

    val allPlaylists by userPlaylistViewModel.allUserPlaylists.collectAsState()
    val isLoading by userPlaylistViewModel.isLoading.collectAsState()
    val error by userPlaylistViewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.sdp).offset(y = 80.sdp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(width = 135.sdp, height = 135.sdp).clip(RoundedCornerShape(20)).background(Color(selectedPlaylist!!.colorHex.toColorInt())),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {}, modifier = Modifier.clip(shape = RoundedCornerShape(20)), colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black
                )) {
                    Icon(painter = painterResource(R.drawable.outline_play_arrow_24), contentDescription = null, tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(10.sdp))
            Text(text = selectedPlaylist!!.title, fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 13.ssp, color = Color.Black, textAlign = TextAlign.Center)
            Text(text = "${playlistTracksResult.size} треків", fontFamily = ArialFamily, fontWeight = FontWeight.Normal, fontSize = 12.ssp, color = Color.Black)

            Spacer(modifier = Modifier.height(30.sdp))
            Text(text = "Треки", textAlign = TextAlign.Left,fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 12.ssp, color = Color.Black)

            if (playlistTracksResult.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 5.sdp)) {
                    items(playlistTracksResult) {
                        UserPlayListTrackItem(
                            track = MusicTrack(id = 0, title = it.title, duration = it.duration, artist = it.artist),
                            onClick = {
                                playerViewModel.setTrackToPlay(it)
                                navController.navigate(Screen.PlayerScreen.route)
                            },
                            addToLiked = {
                                likedViewModel.addToLiked(it)
                                navController.navigate(Screen.LikedScreen.route)
                            }
                        )
                    }
                }
            } else {
                Text(text = "Тут будуть треки плейліста", fontFamily = ArialFamily, fontWeight = FontWeight.Normal, fontSize = 12.ssp, color = Color.Black)
            }

        }
    }
}

@Composable
fun UserPlayListTrackItem(track: MusicTrack, onClick: () -> Unit, addToLiked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.sdp).clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onClick() }, colors = IconButtonDefaults.iconButtonColors(containerColor = colorResource(R.color.light_gray))) {
            Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = colorResource(R.color.play_gray))
        }
        Spacer(modifier = Modifier.width(10.sdp))
        Column(modifier = Modifier.width(120.sdp)) {
            Text(track.title, fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 12.ssp, color = Color.Black)
            Text(track.artist.name, fontFamily = ArialFamily, fontWeight = FontWeight.Normal, fontSize = 10.ssp, color = Color.Black)
        }
        Spacer(modifier = Modifier.width(5.sdp))
        Text(text = formatTime(track.duration.toLong()*1000), fontFamily = ArialFamily, fontWeight = FontWeight.Normal, fontSize = 12.ssp, color = Color.Black)
        Spacer(modifier = Modifier.width(10.sdp))
        /*IconButton(onClick = { addToPlaylistClick() }) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.sdp))
        }*/
        IconButton(onClick = { addToLiked() }) {
            Icon(painter = painterResource(R.drawable.outline_bookmark_heart_24), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.sdp))
        }
    }
}
