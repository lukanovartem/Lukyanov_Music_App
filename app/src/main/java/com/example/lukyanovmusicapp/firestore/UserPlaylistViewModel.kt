package com.example.lukyanovmusicapp.firestore

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lukyanovmusicapp.auth.AuthRepository
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import com.example.lukyanovmusicapp.retrofit.UserPlaylist
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.toString

class UserPlaylistViewModel : ViewModel() {
    private val authRepo = AuthRepository()
    private val playlistRepo = FireStoreOwnPlaylistRepository()

    private val _allUserPlaylists = MutableStateFlow<List<UserPlaylist>>(emptyList())
    val allUserPlaylists: StateFlow<List<UserPlaylist>> = _allUserPlaylists

    private val _selectedPlaylist = MutableStateFlow<UserPlaylist?>(null)
    val selectedPlaylist: StateFlow<UserPlaylist?> = _selectedPlaylist

    private val _playlistTracks = MutableStateFlow<List<MusicTrack>>(emptyList())
    val playlistTracks: StateFlow<List<MusicTrack>> = _playlistTracks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val playlistsColors = listOf("#884A90E2", "#885BCBA3", "#88FFFACD", "#88F08080", "#88CDA4DE", "#8890EE90")

    init {
        getAllPlaylists()
    }

    fun createOwnPlaylist(title: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authRepo.currentUser.value?.uid.toString()
                val playlist = UserPlaylist(title = title, colorHex = playlistsColors.random())
                val created = playlistRepo.createOwnPlaylist(userId, playlist)
                _selectedPlaylist.value = created
                getAllPlaylists() // оновлюю список
            } catch (e: Exception) {
                _errorMessage.value = "Помилка створення: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAllPlaylists() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authRepo.currentUser.value?.uid.toString()
                _allUserPlaylists.value = playlistRepo.getAllPlaylists(userId)
            } catch (e: Exception) {
                _errorMessage.value = "Не вдалося завантажити плейлісти: ${e}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTrackToPlaylist(playlist: UserPlaylist, musicTrack: MusicTrack) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authRepo.currentUser.value?.uid.toString()
                playlistRepo.addTrackToPlaylist(userId, playlist.id, musicTrack)
            } catch (e: Exception) {
                _errorMessage.value = "Не вдалося додати трек до плейліста ${e}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectPlaylist(playlist: UserPlaylist) {
        _selectedPlaylist.value = playlist
        getTracksForPlaylist(playlist)
    }

    private fun getTracksForPlaylist(playlist: UserPlaylist) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authRepo.currentUser.value?.uid.toString()
                val tracks = playlistRepo.getTracksForPlaylist(userId, playlist.id)
                _playlistTracks.value = tracks
            } catch (e: Exception) {
                _errorMessage.value = "Помилка завантаження треків: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
