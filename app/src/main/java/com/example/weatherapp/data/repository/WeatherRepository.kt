package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.ApiResult
import com.example.weatherapp.data.api.OpenWeatherMap
import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.db.entity.CityEntity
import com.example.weatherapp.domain.models.Forecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherMap: OpenWeatherMap
) {

    suspend fun getWeatherByCityName(
        cityName: String,
        localeUnit: String,
        lang: String
    ) = withContext(Dispatchers.IO) {

        flow {
            val userInput = cityName.lowercase()

            weatherDao.getCity(userInput)?.let {
                emit(ApiResult.Success(it))
            }
            emit(ApiResult.Loading())

            try {
                val response = weatherMap.getWeatherByCityName(
                    cityName = userInput,
                    unit = localeUnit,
                    lang = lang
                )
                if (response.isSuccessful) {
                    response.body()?.let {

                        val city = CityEntity(
                            cityId = it.id,
                            cityName = it.name,
                            userInput = userInput,
                            country = it.sys.country,
                            dateTime = it.dt,
                            temperature = it.main.temp,
                            weatherDescription = it.weather[0].description,
                            windDegree = it.wind.deg,
                            windSpeed = it.wind.speed,
                            pressure = it.main.pressure,
                        )
                        weatherDao.deleteCity(userInput)
                        weatherDao.insertCity(city)

                        weatherDao.getCity(userInput)?.let { weather ->
                            emit(ApiResult.Success(weather))
                        } ?: emit(ApiResult.Failure())
                    }
                        ?: emit(ApiResult.Failure())

                } else {
                    emit(ApiResult.Error(userInput))
                }

            } catch (t: Throwable) {
                weatherDao.getCity(userInput)?.let { weather ->
                    emit(ApiResult.Success(weather))
                }
                    ?: emit(ApiResult.Failure())
            }
        }
    }

    suspend fun getFiveDaysForecast(
        cityId: Int,
        localeUnit: String,
        localeLang: String
    ) = withContext(Dispatchers.IO) {

        flow {
            emit(ApiResult.Loading())
            try {
                val response = weatherMap
                    .getFiveDaysForecast(cityId = cityId, unit = localeUnit, lang = localeLang)

                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        emit(ApiResult.Success(Forecast.create(result.list)))
                    } ?: emit(ApiResult.Failure())
                } else {
                    emit(ApiResult.Error())
                }

            } catch (t: Throwable) {
                emit(ApiResult.Failure())
            }
        }
    }
}