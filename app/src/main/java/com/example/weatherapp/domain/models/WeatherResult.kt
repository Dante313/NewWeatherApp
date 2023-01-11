package com.example.weatherapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResult(
    var coord: Coord,
    var weather: List<Weather>,
    var base: String,
    var main: Main,
    var visibility: Int,
    var wind: Wind,
    var clouds: Clouds,
    var dt: Int,
    var sys: Sys,
    var timezone: Int,
    var id: Int,
    var name: String,
    var cod: Int,
)

@Serializable
data class FiveDaysForecastResult(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherList>,
    val city: City
)

@Serializable
data class Sys(
    val type: Int = 0,
    val id: Int = 0,
    val country: String = "",
    val sunrise: Int = 0,
    val sunset: Int = 0,
    val pod: String = ""
)

@Serializable
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

@Serializable
data class Coord(
    val lon: Double,
    val lat: Double
)

@Serializable
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

@Serializable
data class Clouds(
    val all: Int
)

@Serializable
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class WeatherList(
    val dt: Int,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val sys: Sys,
    val dt_txt: String,
)

@Serializable
data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val timezone: Int,
    val sunrise: Int,
    val sunset: Int
)