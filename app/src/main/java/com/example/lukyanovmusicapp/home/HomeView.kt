package com.example.lukyanovmusicapp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lukyanovmusicapp.R
import com.example.lukyanovmusicapp.firestore.UserPlaylistResult
import com.example.lukyanovmusicapp.firestore.UserPlaylistViewModel
import com.example.lukyanovmusicapp.liked.LikedViewModel
import com.example.lukyanovmusicapp.navigation.Screen
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import com.example.lukyanovmusicapp.retrofit.PlayList
import com.example.lukyanovmusicapp.retrofit.UserPlaylist
import com.example.lukyanovmusicapp.ui.theme.ArialFamily
import com.example.lukyanovmusicapp.ui.theme.SatoshiFamily
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(officialPlaylistsViewModel: OfficialPlaylistsViewModel,
             navController: NavHostController,
             userPlaylistViewModel: UserPlaylistViewModel) {
    val officialPlaylistLoadingState by officialPlaylistsViewModel.officialPlaylistsState.collectAsState()
    var showAlertDialog by remember { mutableStateOf(false) }

    var userPlaylistTitleState by remember { mutableStateOf("")}

    val allPlaylists by userPlaylistViewModel.allUserPlaylists.collectAsState()
    val selectedPlaylist by userPlaylistViewModel.selectedPlaylist.collectAsState()
    val playlistTracks by userPlaylistViewModel.playlistTracks.collectAsState()
    val isLoading by userPlaylistViewModel.isLoading.collectAsState()
    val error by userPlaylistViewModel.errorMessage.collectAsState()

    if (showAlertDialog == true) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(240.sdp)
                .clip(RoundedCornerShape(10))
                .background(Color.White),
            onDismissRequest = { showAlertDialog = false },
            confirmButton = {},
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Створити плейліст",
                    fontFamily = SatoshiFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.ssp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.sdp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(0.80f)
                            .height(50.sdp)
                            .border(
                                width = 1.sdp,
                                color = colorResource(R.color.inactive_player_line),
                                shape = RoundedCornerShape(20.sdp)
                            )
                            .clip(RoundedCornerShape(15.sdp)),
                        value = userPlaylistTitleState,
                        onValueChange = { userPlaylistTitleState = it },
                        placeholder = { Text("Назва плейліста", fontSize = 11.ssp) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.height(30.sdp))
                    Button(
                        modifier = Modifier
                            .width(90.sdp)
                            .height(32.sdp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.logo)
                        ),
                        onClick = {
                            if (userPlaylistTitleState.isNotBlank()) {
                                showAlertDialog = false
                                userPlaylistViewModel.createOwnPlaylist(userPlaylistTitleState)
                                userPlaylistViewModel.getAllPlaylists()
                            }

                        }
                    ) {
                        Text(
                            text = "Створити",
                            fontFamily = SatoshiFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.ssp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }





    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .padding(top = 20.sdp, bottom = 20.sdp)
                    .clip(RoundedCornerShape(20))
                    .background(colorResource(R.color.logo)),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(vertical = 15.sdp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.90f),
                        text = "\uD83D\uDC4B Вітаємо!",
                        fontFamily = ArialFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.ssp,
                        color = Color.White
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(0.90f),
                        text = "Раді Вас бачити в застосунку",
                        fontFamily = ArialFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.ssp,
                        color = Color.White
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.90f),
                horizontalArrangement = Arrangement.End
            ) {
                /*Image(
                    modifier = Modifier.size(110.sdp),
                    painter = painterResource(R.drawable.lukyanoff),
                    contentDescription = null
                )*/
            }
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.90f),
                text = "Плейлісти",
                fontFamily = ArialFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.ssp,
                color = Color.Black
            )
        }

        item {
            Text(
                modifier = Modifier.fillMaxWidth(0.90f),
                text = "Офіційні",
                fontFamily = ArialFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.ssp,
                color = Color.Gray
            )
        }

        item {
            when (officialPlaylistLoadingState) {
                OfficialPlaylistsResult.Loading -> {
                    Spacer(modifier = Modifier.height(15.sdp))
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(15.sdp))
                }
                is OfficialPlaylistsResult.Error -> {
                    Spacer(modifier = Modifier.height(15.sdp))
                    Box(modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(135.sdp)
                        .clip(shape = RoundedCornerShape(20))
                        .background(colorResource(R.color.light_gray)), contentAlignment = Alignment.Center) {

                        Text(modifier = Modifier
                            .width(100.sdp)
                            .offset(y = -10.sdp), textAlign = TextAlign.Center,text= "Виникла помилка")

                        Icon(modifier = Modifier.offset(y = 30.sdp), imageVector = Icons.Default.Info, contentDescription = null, tint = Color.Red)

                    }
                    Spacer(modifier = Modifier.height(15.sdp))
                }
                is OfficialPlaylistsResult.Playlists -> {
                    val listOfPlaylists = (officialPlaylistLoadingState as OfficialPlaylistsResult.Playlists).list
                    if (listOfPlaylists.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.sdp)
                        ) {
                            items(listOfPlaylists) { playlist ->
                                OfficialPlaylistItem(playlist, onClick = {
                                    officialPlaylistsViewModel.setSelectedPlaylist(playlist)
                                    officialPlaylistsViewModel.getPlaylistTracks(playlist.id)
                                    officialPlaylistsViewModel.setOfficialTracksFromPlaylistState(
                                        OfficialPlaylistTracksResult.Loading)
                                    navController.navigate(Screen.PlaylistScreen.route)
                                })
                            }
                        }
                    }
                }
                else -> {

                }
            }

        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(0.90f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ваші",
                    fontFamily = ArialFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.ssp,
                    color = Color.Gray
                )
                IconButton (onClick = {
                    showAlertDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }

        item {
            if (allPlaylists.isNotEmpty()) {
                LazyRow (modifier = Modifier.fillMaxWidth()) {
                    items(allPlaylists) { playlist ->
                        UserPlayListItem (playlist, onClick = {
                            userPlaylistViewModel.selectPlaylist(playlist)
                            navController.navigate(Screen.UserPlaylistScreen.route)
                        })
                    }
                }
            }

        }


        item {
            Spacer(modifier = Modifier.height(100.sdp))
        }

    }
}

@Composable
fun UserPlayListItem (
    playlist: UserPlaylist,
    onClick: () -> Unit
) {

    Column (modifier = Modifier
        .wrapContentSize()
        .clickable {onClick()}
        .padding(10.sdp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box (modifier = Modifier
            .width(70.sdp)
            .height(70.sdp)
            .clip(shape = RoundedCornerShape(20))
            .background(Color(playlist.colorHex.toColorInt())), contentAlignment = Alignment.Center) {
            IconButton(onClick = {}, modifier = Modifier.clip(shape = RoundedCornerShape(20)), colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Black
            )) {
                Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.White)
            }
        }
        Text(modifier = Modifier.padding(top = 10.sdp), text = playlist.title, fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 11.ssp, color = Color.Black)

    }


}

@Composable
fun OfficialPlaylistItem(
    playlist: PlayList,
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .wrapContentSize()
        .clickable {
            onClick()
        }
        .padding(10.sdp), horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier
            .width(100.sdp)
            .height(135.sdp)
            .clip(shape = RoundedCornerShape(20))
            .background(Color.LightGray), contentAlignment = Alignment.BottomEnd) {
            Image(modifier = Modifier.matchParentSize(), contentScale = ContentScale.Crop, painter = rememberAsyncImagePainter(playlist.picture_big), contentDescription = null)
            IconButton(onClick = {}, modifier = Modifier.clip(shape = RoundedCornerShape(20)), colors = IconButtonDefaults.iconButtonColors(
                containerColor = colorResource(R.color.light_gray)
            )) {
                Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null, tint = colorResource(R.color.play_gray))
            }
        }
        Text(modifier = Modifier.padding(top = 10.sdp).width(100.sdp),maxLines = 1, overflow = TextOverflow.Ellipsis, text = playlist.title, fontFamily = ArialFamily, fontWeight = FontWeight.Bold, fontSize = 11.ssp, color = Color.Black)
        Text(modifier = Modifier, text = playlist.nb_tracks.toString() + " треків", fontFamily = ArialFamily, fontWeight = FontWeight.Normal, fontSize = 11.ssp, color = Color.Black)
    }

}
