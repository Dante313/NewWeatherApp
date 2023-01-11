package com.example.weatherapp.presenter.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.domain.models.Day
import com.example.weatherapp.presenter.fivedaysforecast.eachdayforecast.DayWeatherFragment

const val FIVE_DAYS_DATA = "data for five days"

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val data: ArrayList<Day> = arrayListOf()

    override fun getItemCount() = data.size

    fun submitData(data: List<Day>) {
        this.data.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }


    override fun createFragment(position: Int): Fragment {
        return if (position < data.size) {
            DayWeatherFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(FIVE_DAYS_DATA, data[position])
                }
            }
        } else Fragment()
    }

}