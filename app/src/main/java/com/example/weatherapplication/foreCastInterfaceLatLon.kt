package com.example.weatherapplication

import androidx.compose.runtime.MutableState
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface foreCastInterfaceLatLon {
    @GET("data/2.5/forecast/daily")
    fun getWeather(@Query("appid") apiKey: String,
                   @Query("lat") latitude: Double,
                   @Query("lon") longitude: Double,
                   @Query("units") unit: String,
                   @Query("cnt") days: Int)
                   : Call<foreCastDataClass>
}