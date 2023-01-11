package com.example.weatherapp.presenter.fivedaysforecast.eachdayforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentDayWeatherBinding
import com.example.weatherapp.domain.models.Day
import com.example.weatherapp.domain.models.DayTime
import com.example.weatherapp.presenter.adapter.FIVE_DAYS_DATA
import com.example.weatherapp.util.convertToTextual

class DayWeatherFragment : Fragment() {
    private lateinit var binding: FragmentDayWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayWeatherBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.getSerializable(FIVE_DAYS_DATA) as Day

        setWeatherData(args)
    }


    private fun setWeatherData(weatherData: Day) {
        val dayTime = listOf(
            R.string.night to DayTime.NIGHT,
            R.string.morning to DayTime.MORNING,
            R.string.day to DayTime.DAY,
            R.string.evening to DayTime.EVENING
        )

        dayTime.forEach { value ->
            val weather = weatherData.getWeather(value.second)

            val tempContainer = layoutInflater.inflate(R.layout.temp_layout, binding.tempContainer)
            fillValue(tempContainer, R.id.txt_daytime, getString(value.first))
            fillValue(
                tempContainer,
                R.id.txt_temp,
                weather?.let { getString(R.string.temperature_degree, it.temp) }
            )

            val feelsLikeContainer = layoutInflater.inflate(
                R.layout.feels_like_temp_layout,
                binding.tempFeelsLikeContainer
            )
            fillValue(
                feelsLikeContainer,
                R.id.txt_temp_feels_like,
                weather?.let { getString(R.string.temperature_degree, it.tempFeelsLike) }
            )

            val windContainer = layoutInflater.inflate(R.layout.wind_layout, binding.windContainer)
            fillValue(windContainer, R.id.txt_daytime, getString(value.first))
            fillValue(
                windContainer,
                R.id.txt_wind,
                weather?.let { getString(R.string.wind_measure, it.windSpeed) }
            )
            fillValue(
                windContainer,
                R.id.txt_wind_up_to,
                weather?.let { getString(R.string.wind_stat_up_to, it.windUpTo) }
            )
            fillValue(
                windContainer, R.id.txt_wind_direction, weather?.windDirection?.convertToTextual(
                    requireActivity()
                )
            )

            val pressureContainer =
                layoutInflater.inflate(R.layout.pressure_layout, binding.pressureContainer)
            fillValue(pressureContainer, R.id.txt_daytime, getString(value.first))
            fillValue(pressureContainer, R.id.txt_pressure, weather?.pressure?.toString())
        }
    }

    private fun fillValue(container: View, id: Int, value: String?) {
        container.findViewById<TextView>(id).apply {
            this.id = View.NO_ID
            value?.let { text = it }
        }
    }
}