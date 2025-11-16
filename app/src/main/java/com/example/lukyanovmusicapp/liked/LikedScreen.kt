package com.example.lukyanovmusicapp.liked


import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.lukyanovmusicapp.R
import com.example.lukyanovmusicapp.home.OfficialPlaylistTracksResult
import com.example.lukyanovmusicapp.navigation.Screen
import com.example.lukyanovmusicapp.player.PlayerViewModel
import com.example.lukyanovmusicapp.playlist.formatTime
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import com.example.lukyanovmusicapp.room.RoomViewModel
import com.example.lukyanovmusicapp.ui.theme.ArialFamily
import kotlinx.coroutines.launch
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun LikedScreen(
    navController: NavController,
    playerViewModel: PlayerViewModel,
    likedViewModel: LikedViewModel,
    roomViewModel: RoomViewModel
) {
    val context = LocalContext.current

    val likedState by likedViewModel.likedState.collectAsState()

    Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.sdp).offset(y = 80.sdp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box (modifier = Modifier
                .width(70.sdp)
                .height(70.sdp)
                .clip(shape = RoundedCornerShape(20))
                .background(Color.Red), contentAlignment = Alignment.Center) {
                IconButton(onClick = {}, modifier = Modifier.clip(shape = RoundedCornerShape(20)), colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black
                )) {
                    Icon(painter = painterResource(R.drawable.heart_1), contentDescription = null, tint = Color.White)
                }
            }
            Text(modifier = Modifier.fillMaxWidth().padding(top = 10.sdp), textAlign = TextAlign.Center,text = "Уподобання", fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 13.ssp, color = Color.Black)

            when (val state = likedState) {
                OfficialPlaylistTracksResult.Loading -> {
                    CircularProgressIndicator()
                }
                is OfficialPlaylistTracksResult.Error -> {
                    Icon(imageVector = Icons.Default.Info, tint = Color.Red, contentDescription = null)
                }
                is OfficialPlaylistTracksResult.tracks -> {
                    val list = state.list
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${list.size} треків",
                        textAlign = TextAlign.Center,
                        fontFamily = ArialFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.ssp,
                        color = Color.Black
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().padding(top = 5.sdp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(list) {
                            LikedPlayListTrackItem(
                                it,
                                onClick = {
                                    playerViewModel.setTrackToPlay(it)
                                    navController.navigate(Screen.PlayerScreen.route)
                                },
                                removeFromLiked = {
                                    likedViewModel.deleteFromLiked(it)
                                },
                                downloadTrack = {
                                    roomViewModel.addTrack(it, context)
                                }
                            )
                        }
                    }
                }
                else -> { /* ничего */ }
            }


        }

    }


}

@Composable
fun LikedPlayListTrackItem(track: MusicTrack, onClick: () -> Unit, downloadTrack: () -> Unit, removeFromLiked: () -> Unit) {
    Row (modifier = Modifier
        .fillMaxWidth(1f)
        .padding(vertical = 10.sdp)
        .clickable {onClick()}, horizontalArrangement = Arrangement.SpaceAround,verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {onClick()}, modifier = Modifier.clip(shape = RoundedCornerShape(20)), colors = IconButtonDefaults.iconButtonColors(
            containerColor = colorResource(R.color.light_gray)
        )) {
            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null, tint = colorResource(R.color.play_gray))
        }
        Spacer(modifier = Modifier.width(10.sdp))
        Column (modifier = Modifier.width(120.sdp)) {
            Text(
                modifier = Modifier,
                text = track.title,
                fontFamily = ArialFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.ssp,
                color = Color.Black
            )
            Text(
                modifier = Modifier,
                text = track.artist.name,
                fontFamily = ArialFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 10.ssp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(5.sdp))
        Text(
            modifier = Modifier,
            text = formatTime(track.duration.toLong() * 1000),
            fontFamily = ArialFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.ssp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(10.sdp))
        IconButton(onClick = {
            downloadTrack()
        }) {
            Icon(
                modifier = Modifier.size(20.sdp),
                painter = painterResource(R.drawable.download),
                contentDescription = null,
                tint = Color.Gray
            )
        }

        IconButton(onClick = {
            removeFromLiked()
        }) {
            Icon(
                modifier = Modifier.size(20.sdp),
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.Red
            )
        }
    }
}
