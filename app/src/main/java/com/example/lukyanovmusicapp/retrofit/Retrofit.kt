package com.example.lukyanovmusicapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


val retrofit = Retrofit.Builder()
    .baseUrl("https://api.deezer.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(RetrofitInterface::class.java)

interface RetrofitInterface {
    @GET("chart/0/playlists")
    suspend fun getOfficialPlaylists(): PlaylistsResponse

    @GET("playlist/{playlist_id}/tracks")
    suspend fun getTracksFromPlaylist(
        @Path("playlist_id") playlistId: Long
    ): PlaylistTracksResponse
}


