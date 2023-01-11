package com.example.weatherapp.presenter.fivedaysforecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.api.ApiResult
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.domain.models.Forecast
import com.example.weatherapp.util.localeRU
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FiveDaysForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _fiveDaysScreenState: MutableLiveData<ApiResult<Forecast>> = MutableLiveData()
    val fiveDaysScreenState: LiveData<ApiResult<Forecast>> = _fiveDaysScreenState

    private var _lastCityId = 0

    fun getFiveDaysForecast(cityId: Int) {

        var localeUnit = "imperial"
        var localeLang = "en"

        if (Locale.getDefault() == localeRU) {
            localeUnit = "metric"
            localeLang = "ru"
        }

        if (_lastCityId == cityId) return
        _lastCityId = cityId

        _fiveDaysScreenState.postValue(ApiResult.Loading())

        viewModelScope.launch {
            weatherRepository.getFiveDaysForecast(cityId, localeUnit, localeLang)
                .onEach { dataState ->
                    _fiveDaysScreenState.postValue(dataState)
                }
                .launchIn(viewModelScope)
        }
    }
}