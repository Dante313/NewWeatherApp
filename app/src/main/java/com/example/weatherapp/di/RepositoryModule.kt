package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.api.OpenWeatherMap
import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.repository.PrefRepository
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(
        database: WeatherDao,
        retrofit: OpenWeatherMap,
    ): WeatherRepository {
        return WeatherRepository(database, retrofit)
    }

    @Singleton
    @Provides
    fun providePrefRepository(
        @ApplicationContext context: Context
    ): PrefRepository {
        return PrefRepository(context)
    }
}