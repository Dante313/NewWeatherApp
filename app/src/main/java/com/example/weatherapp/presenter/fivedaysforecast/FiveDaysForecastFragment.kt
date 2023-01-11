package com.example.weatherapp.presenter.fivedaysforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.R
import com.example.weatherapp.data.api.ApiResult
import com.example.weatherapp.databinding.FragmentFiveDaysForecastBinding
import com.example.weatherapp.domain.models.Day
import com.example.weatherapp.domain.models.Forecast
import com.example.weatherapp.presenter.adapter.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi

@AndroidEntryPoint
class FiveDaysForecastFragment : Fragment() {
    private lateinit var binding: FragmentFiveDaysForecastBinding
    private val args: FiveDaysForecastFragmentArgs by navArgs()

    private val viewModel: FiveDaysForecastViewModel by viewModels()
    private val adapter by lazy {
        PagerAdapter(
            childFragmentManager,
            lifecycle
        )
    }

    @ExperimentalSerializationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFiveDaysForecastBinding.inflate(inflater)
        binding.viewPager.adapter = adapter
        viewModel.getFiveDaysForecast(args.cityId)

        return binding.root
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        binding.toolbar.apply {
            setupWithNavController(navController)
            navigationIcon =
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_arrow_back)
        }

        binding.apply {
            viewModel.fiveDaysScreenState.observe(viewLifecycleOwner) {
                when (it) {
                    is ApiResult.Success<Forecast> -> setSuccessResult(it.data)
                    is ApiResult.Loading<*> -> setLoading()
                    is ApiResult.Error<*> -> setError()
                    is ApiResult.Failure<*> -> setFailure()
                }
            }
        }
    }

    private fun setDaysOfWeek(listOfDays: List<Day>, textView: List<TextView>) {
        for (day in listOfDays.indices)
            textView[day].text = listOfDays[day].getDayOfWeek()
    }

    private fun setSuccessResult(data: Forecast) {
        binding.apply {
            adapter.submitData(data.days)
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                tab.text = data.days[position].getDayOfMonth()
            }.attach()
            setDaysOfWeek(
                data.days,
                listOf(txtFirstDay, txtSecondDay, txtThirdDay, txtFourthDay, txtFifthDay)
            )
            fiveDaysPanel.isVisible = true
            pbFiveDays.isVisible = false
            txtFiveDaysFail.isVisible = false
        }
    }

    private fun setLoading() {
        binding.apply {
            fiveDaysPanel.isVisible = false
            pbFiveDays.isVisible = true
            txtFiveDaysFail.isVisible = false
        }
    }

    private fun setError() {
        binding.apply {
            fiveDaysPanel.isVisible = false
            pbFiveDays.isVisible = false
            txtFiveDaysFail.text = getString(R.string.result_not_succeeded)
            txtFiveDaysFail.isVisible = true
        }
    }

    private fun setFailure() {
        binding.apply {
            fiveDaysPanel.isVisible = false
            pbFiveDays.isVisible = false
            txtFiveDaysFail.text = getString(R.string.internet_error)
            txtFiveDaysFail.isVisible = true
        }
    }
}