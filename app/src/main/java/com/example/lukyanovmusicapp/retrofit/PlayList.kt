package com.example.lukyanovmusicapp.retrofit

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class PlayList (
    val id: Long = 0,
    val title: String = "",
    val nb_tracks: Int = 0,
    val picture_big: String = "",
)

data class UserPlaylist (
    var id: String = "",
    val title: String = "",
    val colorHex: String = "",
)

@Entity(tableName = "offline_tracks")
data class MusicTrack (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "Title")
    val title: String = "",
    @ColumnInfo(name = "Duration")
    val duration: Int = 0,
    @ColumnInfo(name = "Preview")
    val preview: String = "",
    @Embedded(prefix = "Artist_")
    val artist: Artist = Artist(),
    @Embedded(prefix = "Album_")
    val album: Album = Album()
)

data class Artist(
    val name: String = ""
)

data class Album(
    val cover_big: String = ""
)

data class PlaylistsResponse(
    val data: List<PlayList> = emptyList()
)

data class PlaylistTracksResponse(
    val data: List<MusicTrack> = emptyList()
)