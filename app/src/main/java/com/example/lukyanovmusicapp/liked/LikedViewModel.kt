package com.example.lukyanovmusicapp.liked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lukyanovmusicapp.auth.AuthRepository
import com.example.lukyanovmusicapp.firestore.FireStoreLikedRepository
import com.example.lukyanovmusicapp.home.OfficialPlaylistTracksResult
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LikedViewModel: ViewModel() {
    private val _likedState: MutableStateFlow<OfficialPlaylistTracksResult> = MutableStateFlow(OfficialPlaylistTracksResult.Empty)
    val likedState: StateFlow<OfficialPlaylistTracksResult>
        get() = _likedState

    val fireStoreLikedRepository = FireStoreLikedRepository()
    val authRepository = AuthRepository()

    init {
        createLikedPlaylist()
        getLikedPlaylist()
    }

    fun createLikedPlaylist() {
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.uid.toString()
            fireStoreLikedRepository.createLikedPlaylist(userId)
        }
    }

    fun getLikedPlaylist() {
        _likedState.value = OfficialPlaylistTracksResult.Loading
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.uid.toString()
            _likedState.value = fireStoreLikedRepository.getLikedPlaylist(userId)
        }
    }

    fun addToLiked(track: MusicTrack) {
        _likedState.value = OfficialPlaylistTracksResult.Loading
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.uid.toString()
            _likedState.value = fireStoreLikedRepository.addToLiked(track = track, userId = userId)
        }
    }

    fun deleteFromLiked(track: MusicTrack) {
        _likedState.value = OfficialPlaylistTracksResult.Loading
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.uid.toString()
            _likedState.value = fireStoreLikedRepository.deleteFromLiked(track,userId)
        }
    }

}