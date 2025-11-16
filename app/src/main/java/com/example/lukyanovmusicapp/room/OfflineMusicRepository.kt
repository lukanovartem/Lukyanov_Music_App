package com.example.lukyanovmusicapp.room

import com.example.lukyanovmusicapp.retrofit.MusicTrack
import kotlinx.coroutines.flow.Flow

class OfflineMusicRepository (val dao: OfflineMusicDao) {

    suspend fun addTrack(track: MusicTrack) = dao.saveTrack(track)
    suspend fun removeTrack(track: MusicTrack) = dao.deleteTrack(track)

    fun getAllTracks(): Flow<List<MusicTrack>> = dao.getAllTracks()
    fun getTrackById(id: Long): Flow<MusicTrack> = dao.getTrackById(id)
}