package com.example.weatherapplication

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class foreCastModel (
    private val weatherService: foreCastInterface,
    private val apiKey: String,
    private val zipCode: MutableState<Int>,
    private val unit: String,
    private val days: Int,
): ViewModel() {
    private var _foreCastItems: MutableLiveData<foreCastDataClass> = MutableLiveData(null)
    val foreCastItems: LiveData<foreCastDataClass> = _foreCastItems

    var zip2:Int=zipCode.value


    fun fetchWeather() {
        val call = weatherService.getWeather(apiKey, zip2, unit,days)
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