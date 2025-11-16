package com.example.lukyanovmusicapp.firestore

import com.example.lukyanovmusicapp.retrofit.MusicTrack
import com.example.lukyanovmusicapp.retrofit.UserPlaylist

sealed class UserPlaylistResult {
    object Empty: UserPlaylistResult()
    object Loading: UserPlaylistResult()
    data class Error(val error: String): UserPlaylistResult()
    data class Result(
        val userPlaylist: UserPlaylist,
        val tracks: List<MusicTrack>,
        val allUserPlaylists: List<UserPlaylist>
        ): UserPlaylistResult()
}