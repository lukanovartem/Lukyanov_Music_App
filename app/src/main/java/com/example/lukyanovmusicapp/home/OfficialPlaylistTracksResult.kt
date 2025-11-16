package com.example.lukyanovmusicapp.home

import com.example.lukyanovmusicapp.retrofit.MusicTrack
import com.example.lukyanovmusicapp.retrofit.PlayList

sealed class OfficialPlaylistTracksResult {
    object Empty: OfficialPlaylistTracksResult()
    object Loading: OfficialPlaylistTracksResult()
    data class Error(
        val error: String
    ): OfficialPlaylistTracksResult()
    data class tracks(
        var list: List<MusicTrack> = emptyList()
    ): OfficialPlaylistTracksResult()
}

