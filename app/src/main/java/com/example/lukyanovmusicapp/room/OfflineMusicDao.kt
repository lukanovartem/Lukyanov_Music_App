package com.example.lukyanovmusicapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.lukyanovmusicapp.retrofit.MusicTrack
import kotlinx.coroutines.flow.Flow

@Dao()
abstract class OfflineMusicDao {
    @Insert()
    abstract suspend fun saveTrack(
        musicTrack: MusicTrack
    )
    @Delete()
    abstract suspend fun deleteTrack(
        musicTrack: MusicTrack
    )

    @Query("SELECT * FROM `offline_tracks`")
    abstract fun getAllTracks(): Flow<List<MusicTrack>>

    @Query("SELECT * FROM `offline_tracks` WHERE id = :id ")
    abstract fun getTrackById(id: Long): Flow<MusicTrack>

}