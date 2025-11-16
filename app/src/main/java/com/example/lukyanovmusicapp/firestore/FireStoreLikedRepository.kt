package com.example.lukyanovmusicapp.firestore

import android.util.Log
import com.example.lukyanovmusicapp.home.OfficialPlaylistTracksResult
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import com.example.lukyanovmusicapp.retrofit.PlayList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FireStoreLikedRepository {
    val firestore = FirebaseFirestore.getInstance()

    //Liked playlist
    suspend fun createLikedPlaylist(
        userId: String
    ) {
        try {
            val documentHref = firestore.collection("Liked Playlists").document(userId)
            val snapshot = documentHref.get().await()

            if (!snapshot.exists()) {
                documentHref.set(PlayList(title = "Liked Songs", picture_big = "https://static.vecteezy.com/system/resources/previews/009/400/889/non_2x/red-heart-set-clipart-design-illustration-free-png.png"))
                    .await()
            }

        } catch (e: Exception) {
             Log.d("liked","error creating the liked playlist")

        }
    }

    suspend fun addToLiked(
        track: MusicTrack,
        userId: String
    ): OfficialPlaylistTracksResult {
        return try {
            val userPlaylistHref = firestore.collection("Liked Playlists").document(userId)
            userPlaylistHref.collection("tracks").add(track).await()
            getLikedPlaylist(userId)
        }
        catch (e: Exception) {
            Log.d("liked","error adding track to liked")
            OfficialPlaylistTracksResult.Error(e.toString())
        }
    }

    suspend fun getLikedPlaylist(
        userId: String
    ): OfficialPlaylistTracksResult {
        return try {
            val userPlaylistHref = firestore.collection("Liked Playlists").document(userId).collection("tracks")
            val likedTracks = userPlaylistHref.get().await()
            OfficialPlaylistTracksResult.tracks(likedTracks.toObjects(MusicTrack::class.java))
        }
        catch (e: Exception) {
            Log.d("liked","error getting the liked playlist")
            OfficialPlaylistTracksResult.Error(e.toString())
        }
    }

    suspend fun deleteFromLiked(
        track: MusicTrack,
        userId: String
    ): OfficialPlaylistTracksResult {
        return try {
            val userPlaylistHref = firestore.collection("Liked Playlists")
                .document(userId)
                .collection("tracks")

            val querySnapshot = userPlaylistHref.get().await()

            val trackToDelete = querySnapshot.documents.firstOrNull { document ->
                val musicTrack = document.toObject(MusicTrack::class.java)
                musicTrack?.id == track.id
            }

            if (trackToDelete != null) {
                trackToDelete.reference.delete().await()
            } else {
                Log.d("liked", "Track not found for deletion: ${track.title}")
            }

            getLikedPlaylist(userId)
        } catch (e: Exception) {
            Log.d("liked", "error deleting track from liked", e)
            OfficialPlaylistTracksResult.Error(e.toString())
        }
    }

}