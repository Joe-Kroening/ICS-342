package com.example.weatherapplication

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class foreCastModelLatLon (
    private val weatherService: foreCastInterfaceLatLon,
    private val apiKey: String,
    private val unit: String,
    private val days: Int,
    private val latitude: MutableState<Double>,
    private val longitude: MutableState<Double>
): ViewModel() {
    private var _foreCastItems: MutableLiveData<foreCastDataClass> = MutableLiveData(null)
    val foreCastItems: LiveData<foreCastDataClass> = _foreCastItems


    fun fetchWeather() {
        val call = weatherService.getWeather(apiKey, latitude.value,longitude.value , unit,days)
        call.enqueue(object: Callback<foreCastDataClass> {
            override fun onResponse(p0: Call<foreCastDataClass>, p1: Response<foreCastDataClass>) {
                _foreCastItems.value = p1.body()
            }

            override fun onFailure(p0: Call<foreCastDataClass>, p1: Throwable) {
                Log.e("Weather", "Failure fetching Weather", p1)
            }
        })
    }
}