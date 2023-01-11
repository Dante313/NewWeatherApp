package com.example.weatherapp.domain.models

import com.example.weatherapp.util.getTime
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Forecast private constructor() : Serializable {
    private val _days: MutableList<Day> = mutableListOf()
    val days: List<Day> = _days

    companion object {
        fun create(weather: List<WeatherList>): Forecast {
            val forecast = Forecast()
            val daysTemp = weather
                .groupBy {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.time = Date(it.dt * 1000L)
                    calendar.get(Calendar.DAY_OF_MONTH)
                }
                .map {
                    Day.create(it.value)
                }
                .take(5)
            forecast._days.addAll(daysTemp)

            return forecast
        }
    }
}


class Day private constructor() : Serializable {
    private val _weatherData: MutableMap<DayTime, WeatherData> = mutableMapOf()
    private var _date: Int = 0

    private val NIGHT_DAYTIME = 0
    private val MORNING_DAYTIME = 6
    private val DAY_DAYTIME = 12
    private val EVENING_DAYTIME = 18

    companion object {
        fun create(weather: List<WeatherList>): Day {
            val myDay = Day()

            weather.forEach {
                for (hour in it.weather.indices) {
                    val timeWeather = WeatherData.create(it)

                    when (it.getTime()) {
                        myDay.NIGHT_DAYTIME -> myDay._weatherData[DayTime.NIGHT] = timeWeather
                        myDay.MORNING_DAYTIME -> myDay._weatherData[DayTime.MORNING] = timeWeather
                        myDay.DAY_DAYTIME -> myDay._weatherData[DayTime.DAY] = timeWeather
                        myDay.EVENING_DAYTIME -> myDay._weatherData[DayTime.EVENING] = timeWeather
                    }
                }
                myDay._date = it.dt
            }
            return myDay
        }
    }

    fun getWeather(dayTime: DayTime) = _weatherData[dayTime]

    fun getDayOfMonth(): String {
        val formatter = SimpleDateFormat("d", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(_date.toLong() * 1000)
    }

    fun getDayOfWeek(): String {
        val formatter = SimpleDateFormat("E", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(_date.toLong() * 1000)
    }
}


class WeatherData(
    val temp: Double = 0.0,
    val tempFeelsLike: Double = 0.0,
    val windSpeed: Double = 0.0,
    val windUpTo: Double = 0.0,
    val windDirection: Int = 0,
    val pressure: Int = 0,
) : Serializable {

    companion object {
        fun create(weather: WeatherList) =
            WeatherData(
                weather.main.temp,
                weather.main.feels_like,
                weather.wind.speed,
                weather.wind.gust,
                weather.wind.deg,
                weather.main.pressure,
            )

    }
}


enum class DayTime {
    NIGHT, MORNING, DAY, EVENING
}