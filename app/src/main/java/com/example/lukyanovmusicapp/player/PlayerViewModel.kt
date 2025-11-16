package com.example.lukyanovmusicapp.player

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.lukyanovmusicapp.retrofit.MusicTrack

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    ////
    private val _trackToPlay = mutableStateOf(MusicTrack())
    val trackToPlay
        get() = _trackToPlay
    ///////

    private val context = application.applicationContext
    private val player = ExoPlayer.Builder(context).build()

    private val handler = Handler(Looper.getMainLooper())

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying

    private val _position = mutableStateOf(0L)
    val position: State<Long> = _position

    private val _duration = mutableStateOf(0L)
    val duration: State<Long> = _duration


    fun setTrackToPlay(track: MusicTrack) {
        trackToPlay.value = track
        player.stop()
        player.clearMediaItems()

        val mediaItem = MediaItem.fromUri(track.preview)
        player.setMediaItem(mediaItem)
        player.prepare()

        Log.d("PlayerViewModel", "Prepared: ${track.title} ${track.preview}")

        player.play()
    }



    /////
    private val updateRunnable = object : Runnable {
        override fun run() {
            _position.value = player.currentPosition
            handler.postDelayed(this, 1000)
        }
    }

    init {
        val mediaItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
        player.setMediaItem(mediaItem)
        player.prepare()

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                _duration.value = player.duration
            }
        })

        handler.post(updateRunnable)
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(updateRunnable)
        player.release()
    }
}
