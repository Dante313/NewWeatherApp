package com.example.weatherapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.weatherapp.data.db.entity.CityEntity

@Dao
interface WeatherDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertCity(cityEntity: CityEntity)

    @Query("DELETE FROM city WHERE user_input = :userInput")
    suspend fun deleteCity(userInput: String)

    @Query("SELECT * FROM city WHERE user_input = :userInput")
    suspend fun getCity(userInput: String): CityEntity?
}