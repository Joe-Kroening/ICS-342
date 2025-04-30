package com.example.weatherapplication

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class notificationModel (
    private val weatherService: notificationInterface,
    private val apiKey: String,
    public var latitude:MutableState<Double>,
    //private var latitude: Double,
    public var longitude:MutableState<Double>,
    //private val longitude: Double,
    private val unit: String,

): ViewModel() {
    private var _weatherItems: MutableLiveData<weatherDataClass> = MutableLiveData(null)
    val weatherItems: LiveData<weatherDataClass> = _weatherItems


    fun getApi(): String {
        return apiKey
    }

    fun updateLat(newLat: Double) {
        latitude.value=newLat
    }

    fun updateLong(newLong: Double) {
        longitude.value=newLong
    }



    //!NEW
    // var zipCode: MutableLiveData<Int> = MutableLiveData(55155)
    //var zip2: LiveData<Int> = zipCodeState
   // var zip2:Int=zipCodeState.value


  //  fun getZip(): Int? {
  //      return zipCodeState.value
  //  }



    //! END OF NEW


    fun fetchWeather() {
        val call = weatherService.getWeather(apiKey, latitude.value,longitude.value, unit)
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