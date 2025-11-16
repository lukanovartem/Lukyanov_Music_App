package com.example.lukyanovmusicapp.playlist


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
import com.example.lukyanovmusicapp.retrofit.UserPlaylist
import com.example.lukyanovmusicapp.ui.theme.ArialFamily
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun PlayListView(navController: NavHostController,
                 officialPlaylistsViewModel: OfficialPlaylistsViewModel,
                 playerViewModel: PlayerViewModel,
                 likedViewModel: LikedViewModel,
                 userPlaylistViewModel: UserPlaylistViewModel)
{
    var showAlertDialog by remember { mutableStateOf(false) }
    val playlistTracksResult = officialPlaylistsViewModel.officialTracksFromPlaylist.collectAsState()
    val selectedPlaylist by officialPlaylistsViewModel.selectedPlaylist

    var selectedTrack by remember {mutableStateOf(MusicTrack())}
    val userPlaylistTracksResult by userPlaylistViewModel.playlistTracks.collectAsState()
    val selectedUserPlaylist by userPlaylistViewModel.selectedPlaylist.collectAsState()

    val allUserPlaylists by userPlaylistViewModel.allUserPlaylists.collectAsState()
    val isLoading by userPlaylistViewModel.isLoading.collectAsState()
    val error by userPlaylistViewModel.errorMessage.collectAsState()

    if (showAlertDialog) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth().height(410.sdp).clip(RoundedCornerShape(10)).background(Color.White),
            onDismissRequest = { showAlertDialog = false },
            confirmButton = {},
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Додати до плейліста",
                    fontFamily = SatoshiFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.ssp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.sdp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Заглушка для трека
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15))
                            .background(colorResource(R.color.light_gray))
                            .padding(vertical = 10.sdp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {}, modifier = Modifier.clip(RoundedCornerShape(20)),
                            colors = IconButtonDefaults.iconButtonColors(containerColor = colorResource(R.color.logo))
                        ) {
                            Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = colorResource(R.color.black))
                        }
                        Spacer(modifier = Modifier.width(10.sdp))
                        Column(modifier = Modifier.width(120.sdp)) {
                            Text(selectedTrack.title, fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 12.ssp, color = Color.Black)
                            Text(selectedTrack.artist.name, fontFamily = ArialFamily, fontWeight = FontWeight.Normal, fontSize = 10.ssp, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(5.sdp))
                        Text(formatTime(selectedTrack.duration.toLong()*1000), fontFamily = ArialFamily, fontWeight = FontWeight.Normal, fontSize = 12.ssp, color = Color.Black)
                        Spacer(modifier = Modifier.width(15.sdp))
                    }

                    Spacer(modifier = Modifier.height(10.sdp))

                    LazyRow(modifier = Modifier.fillMaxWidth(0.90f)) {
                        items(allUserPlaylists) {
                            UserPlayListItemAddButton(
                                playlist = it,
                                addToPlaylistClick = {
                                    showAlertDialog = false
                                    userPlaylistViewModel.addTrackToPlaylist(it, selectedTrack)
                                    userPlaylistViewModel.getAllPlaylists()
                                }
                            )
                        }
                    }
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.sdp).offset(y = 80.sdp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(width = 230.sdp, height = 135.sdp).clip(RoundedCornerShape(20)).background(Color.LightGray),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, painter = rememberAsyncImagePainter(selectedPlaylist.picture_big), contentDescription = null)
            }
            Spacer(modifier = Modifier.height(5.sdp))
            Text(text = selectedPlaylist.title, fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 13.ssp, color = Color.Black, textAlign = TextAlign.Center)
            Text(text = selectedPlaylist.nb_tracks.toString() + "треків", fontFamily = ArialFamily, fontWeight = FontWeight.Normal, fontSize = 12.ssp, color = Color.Black, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(30.sdp))
            Text(text = "Треки", textAlign = TextAlign.Left,fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 12.ssp, color = Color.Black)



            when (val result = playlistTracksResult.value) {
                is OfficialPlaylistTracksResult.tracks -> {
                    if (result.list.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 5.sdp)) {
                            items(result.list) {
                                PlayListTrackItem(
                                    track = MusicTrack(id = 0, title = it.title, duration = it.duration, artist = it.artist),
                                    onClick = {
                                        playerViewModel.setTrackToPlay(it)
                                        navController.navigate(Screen.PlayerScreen.route)
                                    },
                                    addToPlaylistClick = {
                                        selectedTrack = it
                                        showAlertDialog = true
                                                         },
                                    addToLiked = {
                                        likedViewModel.addToLiked(it)
                                        navController.navigate(Screen.LikedScreen.route)
                                    }
                                )
                            }
                        }
                    }
                }
                is OfficialPlaylistTracksResult.Error -> {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color.Red)
                    Text("Error: ${result.error}", color = Color.Red)
                }
                OfficialPlaylistTracksResult.Empty -> {
                    Text("No tracks found", color = Color.Gray)
                }
                OfficialPlaylistTracksResult.Loading -> {
                    CircularProgressIndicator()
                    Text("Loading", color = Color.Gray)
                }
            }

        }
    }
}

@Composable
fun UserPlayListItemAddButton(
    playlist: UserPlaylist,
    addToPlaylistClick: () -> Unit
) {
    Column(modifier = Modifier.wrapContentSize().clickable {}.padding(10.sdp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(70.sdp).clip(RoundedCornerShape(20)).background(Color(playlist.colorHex.toColorInt())),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = {}, colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black)) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.White)
            }
        }
        Text(playlist.title, fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 11.ssp, color = Color.Black)
        IconButton(onClick = { addToPlaylistClick() }, colors = IconButtonDefaults.iconButtonColors(containerColor = colorResource(R.color.white))) {
            Icon(Icons.Rounded.Add, contentDescription = null, tint = Color.Black)
        }
    }
}

@Composable
fun PlayListTrackItem(track: MusicTrack, onClick: () -> Unit, addToPlaylistClick: () -> Unit, addToLiked: () -> Unit) {
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
        IconButton(onClick = { addToPlaylistClick() }) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.sdp))
        }
        IconButton(onClick = { addToLiked() }) {
            Icon(painter = painterResource(R.drawable.outline_bookmark_heart_24), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.sdp))
        }
    }
}

fun formatTime(ms: Long): String {
    val minutes = ms / 1000 / 60
    val seconds = ms / 1000 % 60
    return String.format("%d:%02d", minutes, seconds)
}
