package com.example.weatherapp.presenter.citysearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.api.ApiResult
import com.example.weatherapp.data.db.entity.CityEntity
import com.example.weatherapp.data.repository.PrefRepository
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.util.localeRU
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val prefRepository: PrefRepository,
) : ViewModel() {

    private val _screenState: MutableLiveData<ApiResult<CityEntity>> = MutableLiveData()
    val screenState: LiveData<ApiResult<CityEntity>> = _screenState

    private var lastInput: String = ""
    var cityId: Int = 0

    val getUserInput = prefRepository.getUserInput()

    private var job: Job? = null


    fun getWeatherByCityName(
        cityName: String
    ) {

        if (lastInput == cityName) return
        lastInput = cityName

        job?.cancel()

        var localeUnit = "imperial"
        var localeLang = "en"

        if (Locale.getDefault() == localeRU) {
            localeUnit = "metric"
            localeLang = "ru"
        }

        job = viewModelScope.launch {
            weatherRepository.getWeatherByCityName(cityName, localeUnit, localeLang)
                .onEach { dataState ->
                    when (dataState) {
                        is ApiResult.Success -> {
                            prefRepository.setUserInput(cityName)
                            cityId = dataState.data.cityId
                            _screenState.value = dataState
                        }
                        else -> _screenState.value = dataState
                    }
                }
                .launchIn(viewModelScope)
        }
    }
}