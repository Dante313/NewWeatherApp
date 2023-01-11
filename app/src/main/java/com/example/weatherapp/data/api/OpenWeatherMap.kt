package com.example.weatherapp.data.api

import com.example.weatherapp.domain.models.FiveDaysForecastResult
import com.example.weatherapp.domain.models.WeatherResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "d0b49b5263481028d57c9ed3053154ee"

interface OpenWeatherMap {
    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("appid") appId: String = API_KEY,
        @Query("units") unit: String,
        @Query("lang") lang: String
    ): Response<WeatherResult>

    @GET("forecast")
    suspend fun getFiveDaysForecast(
        @Query("id") cityId: Int,
        @Query("appid") appId: String = API_KEY,
        @Query("units") unit: String,
        @Query("lang") lang: String
    ): Response<FiveDaysForecastResult>
}