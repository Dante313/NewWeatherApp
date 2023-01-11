package com.example.weatherapp.util

import android.content.Context
import com.example.weatherapp.R
import com.example.weatherapp.domain.models.WeatherList
import java.text.SimpleDateFormat
import java.util.*

val localeRU = Locale("ru", "RU")

fun Int.convertUnixToDate(): String {
    val sdf: SimpleDateFormat = if (Locale.getDefault() == localeRU) {
        SimpleDateFormat("dd.MM.yyyy HH:mm", localeRU)
    } else {
        SimpleDateFormat("MM.dd.yyyy KK:mm a", Locale.US)
    }
    return sdf.format(Date(this * 1000L))
}

fun Int.convertToTextual(context: Context): String =
    when {
        (this > 337.5) -> context.getString(R.string.N)
        (this > 292.5) -> context.getString(R.string.NW)
        (this > 247.5) -> context.getString(R.string.W)
        (this > 202.5) -> context.getString(R.string.SW)
        (this > 157.5) -> context.getString(R.string.S)
        (this > 112.5) -> context.getString(R.string.SE)
        (this > 67.5) -> context.getString(R.string.E)
        (this > 22.5) -> context.getString(R.string.NE)
        else -> context.getString(R.string.N)
    }

fun WeatherList.getTime(): Int {
    val formatter = SimpleDateFormat("HH", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(this.dt.toLong() * 1000).toInt()
}