package com.example.lukyanovmusicapp.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import com.example.lukyanovmusicapp.retrofit.PlayList
import com.example.lukyanovmusicapp.retrofit.PlaylistsResponse
import com.example.lukyanovmusicapp.retrofit.apiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OfficialPlaylistsViewModel: ViewModel() {

    // Result of getting for ui
    private val _officialPlaylistsState = MutableStateFlow<OfficialPlaylistsResult>(OfficialPlaylistsResult.Empty)
    val officialPlaylistsState: StateFlow<OfficialPlaylistsResult>
        get() = _officialPlaylistsState

    private val _officialTracksFromPlaylist = MutableStateFlow<OfficialPlaylistTracksResult>(OfficialPlaylistTracksResult.Empty)
    val officialTracksFromPlaylist: StateFlow<OfficialPlaylistTracksResult>
        get() = _officialTracksFromPlaylist

    fun setOfficialTracksFromPlaylistState (result: OfficialPlaylistTracksResult) {
        _officialTracksFromPlaylist.value = result
    }

    //Selected playlist
    private val _selectedPlaylist= mutableStateOf(PlayList())
    val selectedPlaylist
        get() = _selectedPlaylist

    fun setSelectedPlaylist (playlist: PlayList) {
        _selectedPlaylist.value = playlist
        Log.d("myapp", selectedPlaylist.toString())
    }


    init {
        _officialPlaylistsState.value = OfficialPlaylistsResult.Loading
        viewModelScope.launch {
            try {
                val playlists = apiService.getOfficialPlaylists().data
                _officialPlaylistsState.value = OfficialPlaylistsResult.Playlists(playlists)
            } catch (e: Exception) {
                _officialPlaylistsState.value = OfficialPlaylistsResult.Error(e.toString())
            }
        }
    }

    fun getPlaylistTracks (id: Long) {
        viewModelScope.launch {
            try {
                val playlistTracks = apiService.getTracksFromPlaylist(id).data
                _officialTracksFromPlaylist.value = OfficialPlaylistTracksResult.tracks(playlistTracks)
            } catch (e: Exception) {
                _officialTracksFromPlaylist.value = OfficialPlaylistTracksResult.Error(e.toString())
            }
        }
    }


}