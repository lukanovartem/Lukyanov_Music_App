package com.example.lukyanovmusicapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lukyanovmusicapp.retrofit.MusicTrack

@Database(
    version = 1,
    entities = [MusicTrack::class],
    exportSchema = false
)
abstract class MusicDatabase(): RoomDatabase() {
    abstract fun musicDao(): OfflineMusicDao
}