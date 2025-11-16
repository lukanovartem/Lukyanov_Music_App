package com.example.lukyanovmusicapp.room

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class RoomViewModel: ViewModel() {
    val repository by Graph.musicRepository
    lateinit var allTracks: Flow<List<MusicTrack>>

    init {
        getAllTracks()
    }

    fun getAllTracks() {
        allTracks = repository.getAllTracks()
    }

    fun addTrack(track: MusicTrack, context: Context) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val url = track.preview
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connect()

                    val fileName = "track_${track.id}.mp3"
                    val file = File(context.filesDir, fileName)

                    connection.inputStream.use { input ->
                        FileOutputStream(file).use { output ->
                            input.copyTo(output)
                        }
                    }

                    val updatedTrack = track.copy(preview = file.absolutePath)

                    // сохраняем в Room прямо на IO
                    repository.addTrack(updatedTrack)

                    Log.d("file", "Track saved to: ${file.absolutePath}")
                }
            } catch (e: Exception) {
                Log.e("file", "Error downloading file", e)
            }
        }
    }

    fun removeTrack(musicTrack: MusicTrack) {
        viewModelScope.launch {
            repository.removeTrack(musicTrack)
        }
    }

    fun getTrackById(id: Long): Flow<MusicTrack> {
        return repository.getTrackById(id)
    }

}