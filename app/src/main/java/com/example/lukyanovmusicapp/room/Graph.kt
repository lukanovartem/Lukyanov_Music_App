package com.example.lukyanovmusicapp.room

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: MusicDatabase
    val musicRepository = lazy {
        OfflineMusicRepository(database.musicDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, MusicDatabase::class.java, "offline_music.db").build()
    }
}