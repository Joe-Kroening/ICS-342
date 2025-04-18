package com.example.weatherapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface weatherInterface {

    @GET("data/2.5/weather")
    fun getWeather(@Query("appid") apiKey: String,
                   @Query("zip") zipCode:Int,
                   @Query("units") unit: String)
    : Call<weatherDataClass>

}