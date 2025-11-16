package com.example.lukyanovmusicapp.firestore

import android.util.Log
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import com.example.lukyanovmusicapp.retrofit.PlayList
import com.example.lukyanovmusicapp.retrofit.UserPlaylist
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.tasks.await

class FireStoreOwnPlaylistRepository {
    private val firebase = FirebaseFirestore.getInstance()

    suspend fun createOwnPlaylist(userId: String, playlist: UserPlaylist): UserPlaylist {
        val playlistRef = firebase.collection("Users Playlists")
            .document(userId)
            .collection("playlists")
            .document()
        playlist.id = playlistRef.id
        playlistRef.set(playlist).await()
        return playlist
    }

    suspend fun getAllPlaylists(userId: String): List<UserPlaylist> {
        val snapshot = firebase.collection("Users Playlists")
            .document(userId)
            .collection("playlists")
            .get()
            .await()
        return snapshot.toObjects(UserPlaylist::class.java)
    }

    suspend fun getTracksForPlaylist(userId: String, playlistId: String): List<MusicTrack> {
        val tracks = firebase.collection("Users Playlists")
            .document(userId)
            .collection("playlists")
            .document(playlistId)
            .collection("tracks")
            .get()
            .await()
        return tracks.toObjects(MusicTrack::class.java)
    }

    suspend fun addTrackToPlaylist(
        userId: String, playlistId: String, musicTrack: MusicTrack
    ) {
        firebase.collection("Users Playlists")
            .document(userId)
            .collection("playlists")
            .document(playlistId)
            .collection("tracks")
            .add(musicTrack)
            .await()
    }
}
