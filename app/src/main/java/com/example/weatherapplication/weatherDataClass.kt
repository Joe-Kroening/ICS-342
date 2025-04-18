package com.example.weatherapplication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@JsonIgnoreUnknownKeys
@Serializable
data class weatherDataClass(
    val coord: latLong,
    val weather: List<weatherData>,
    val main: mainData,
    val dt: Int,
    val name: String,

)
@JsonIgnoreUnknownKeys
@Serializable
data class latLong(
    val lon: Double,
    val lat: Double
)
@JsonIgnoreUnknownKeys
@Serializable
data class weatherData(
    val id: Double,
    val main: String,
    val description: String,
    val icon: String
)
@JsonIgnoreUnknownKeys
@Serializable
data class mainData(
    val temp: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    @SerialName("temp_min")
    val minTemp: Double,
    @SerialName("temp_max")
    val maxTemp: Double,
    val humidity: Double,
    val pressure: Double,

)



