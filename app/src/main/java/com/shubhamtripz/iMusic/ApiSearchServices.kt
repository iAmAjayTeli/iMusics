package com.shubhamtripz.mintsongspodcast

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSearchServices {

    @GET("exec?action=getUsers")
    fun getUsersByTitle(@Query("title") title: String): Call<List<Popular_song_model>>

    companion object {
        private const val BASE_URL = "https://script.google.com/macros/s/AKfycbwDF1ZUyLvTZi7z5pF_P6pqu6oDzqxuvCjKMNJCN8U797hnNH2csUzer9hepmKQb8CPLQ/"

        fun create(): ApiSearchServices {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiSearchServices::class.java)
        }
    }
}