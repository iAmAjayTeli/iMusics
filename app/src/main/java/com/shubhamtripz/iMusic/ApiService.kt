package com.shubhamtripz.mintsongspodcast


import retrofit2.Call
import retrofit2.http.GET

interface  ApiService {
    @GET("exec?action=getUsers")
   fun  getUsers(): Call<List<Popular_song_model>>
}