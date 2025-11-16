package com.example.lukyanovmusicapp.home

import com.example.lukyanovmusicapp.retrofit.PlayList

sealed class OfficialPlaylistsResult {
    object Empty: OfficialPlaylistsResult()
    object Loading: OfficialPlaylistsResult()
    data class Error(
        val error: String
    ): OfficialPlaylistsResult()
    data class Playlists(
        var list: List<PlayList> = emptyList()
    ): OfficialPlaylistsResult()
}