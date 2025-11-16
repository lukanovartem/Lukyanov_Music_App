package com.example.lukyanovmusicapp.room

import android.app.Application

class MusicApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}