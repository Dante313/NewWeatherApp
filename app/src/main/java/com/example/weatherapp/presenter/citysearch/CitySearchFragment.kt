package com.example.weatherapp.presenter.citysearch

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.data.api.ApiResult
import com.example.weatherapp.data.db.entity.CityEntity
import com.example.weatherapp.databinding.FragmentCitySearchBinding
import com.example.weatherapp.util.convertToTextual
import com.example.weatherapp.util.convertUnixToDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi

const val DELAY = 500L

@AndroidEntryPoint
class CitySearchFragment : Fragment() {
    private lateinit var binding: FragmentCitySearchBinding
    private val handler = Handler(Looper.getMainLooper())
    private val viewModel: CitySearchViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCitySearchBinding.inflate(inflater)

        return binding.root
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading<*> -> setLoading()
                is ApiResult.Error<*> -> setErrorResult(it.city)
                is ApiResult.Failure<*> -> setFailureResult()
                is ApiResult.Success<CityEntity> -> setSuccessfulResult(it.data)
            }
        }

        binding.etCitySearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeCallbacksAndMessages(null)
                if (text?.isNotEmpty() == true) {
                    val searchCity = Runnable {
                        viewModel.getWeatherByCityName(text.toString())
                    }
                    handler.postDelayed(searchCity, DELAY)
                }
            }

            override fun afterTextChanged(text: Editable?) {
            }
        })

        binding.weatherPanel.setOnClickListener {
            closeKeyboard(requireContext(), view)

            view.findNavController().navigate(
                CitySearchFragmentDirections.actionCitySearchFragmentToFiveDaysForecastFragment(
                    viewModel.cityId
                )
            )
        }

        binding.etCitySearch.apply {
            setText(viewModel.getUserInput)
            setSelection(length())
        }
    }

    private fun setSuccessfulResult(data: CityEntity) {
        binding.apply {
            txtCityName.text = data.cityName
            txtCountryName.text = data.country
            txtDateTime.text = data.dateTime.convertUnixToDate()
            txtTemperature.text =
                getString(R.string.temperature, data.temperature)
            txtWeatherMain.text = data.weatherDescription
            txtWind.text = getString(
                R.string.wind_stat,
                data.windDegree.convertToTextual(requireActivity()),
                data.windSpeed
            )
            txtPressure.text =
                getString(R.string.pressure_stat, data.pressure)

            weatherPanel.visibility = View.VISIBLE
            txtCitySearchFail.visibility = View.INVISIBLE
            pbCitySearch.visibility = View.INVISIBLE
        }
    }

    private fun setErrorResult(city: String) {
        binding.apply {
            weatherPanel.visibility = View.GONE
            txtCitySearchFail.text =
                getString(R.string.city_not_found, city)
            txtCitySearchFail.visibility = View.VISIBLE
            pbCitySearch.visibility = View.INVISIBLE
        }
    }

    private fun setFailureResult() {
        binding.apply {
            weatherPanel.visibility = View.GONE
            txtCitySearchFail.text =
                getString(R.string.internet_error)
            txtCitySearchFail.visibility = View.VISIBLE
            pbCitySearch.visibility = View.INVISIBLE
        }
    }

    private fun setLoading() {
        binding.apply {
            txtCitySearchFail.visibility = View.INVISIBLE
            pbCitySearch.visibility = View.VISIBLE
        }
    }

    private fun closeKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}