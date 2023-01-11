package com.example.weatherapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    val rowId: Int = 0,

    @ColumnInfo(name = "city_id")
    val cityId: Int,

    @ColumnInfo(name = "user_input")
    val userInput: String,

    @ColumnInfo(name = "city_name")
    val cityName: String,

    @ColumnInfo(name = "country")
    val country: String,

    @ColumnInfo(name = "date_time")
    val dateTime: Int,

    @ColumnInfo(name = "temperature")
    val temperature: Double,

    @ColumnInfo(name = "weather_description")
    val weatherDescription: String,

    @ColumnInfo(name = "wind_degree")
    val windDegree: Int,

    @ColumnInfo(name = "wind_speed")
    val windSpeed: Double,

    @ColumnInfo(name = "pressure")
    val pressure: Int,
)