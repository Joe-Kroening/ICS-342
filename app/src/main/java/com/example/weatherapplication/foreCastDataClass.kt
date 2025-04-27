package com.example.weatherapplication

import android.health.connect.datatypes.units.Pressure
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@JsonIgnoreUnknownKeys
@Serializable
data class foreCastDataClass(
    var city: city,
    var list: List <list> = listOf()
    )

@JsonIgnoreUnknownKeys
@Serializable
data class city(
    var name: String? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class list(
    var dt: Long?=null,
    var temp: Temp,
    var pressure: Int?=null,
    var humidity: Int?=null,
    var weather: List<Weather> = listOf()
)

@JsonIgnoreUnknownKeys
@Serializable
data class Temp(
    val min: Double?=null,
    val max: Double?=null,
)

@JsonIgnoreUnknownKeys
@Serializable
data class Weather(
    val main: String?=null,
    val id: Double?=null
)
