package com.example.weatherapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherModel (
    private val weatherService: weatherInterface,
    private val apiKey: String,
    private val zipCode: Int,
    private val unit: String,
    ): ViewModel() {
        private var _weatherItems: MutableLiveData<weatherDataClass> = MutableLiveData(null)
        val weatherItems: LiveData<weatherDataClass> = _weatherItems


        fun fetchWeather() {
            val call = weatherService.getWeather(apiKey, zipCode, unit)
            call.enqueue(object: Callback<weatherDataClass> {
                override fun onResponse(p0: Call<weatherDataClass>, p1: Response<weatherDataClass>) {
                    _weatherItems.value = p1.body()
                }

                override fun onFailure(p0: Call<weatherDataClass>, p1: Throwable) {
                    Log.e("Weather", "Failure fetching Weather", p1)
                }
            })
        }
    }