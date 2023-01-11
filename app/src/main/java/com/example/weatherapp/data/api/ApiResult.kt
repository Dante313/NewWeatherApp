package com.example.weatherapp.data.api

sealed class ApiResult<T> {
    class Success<T>(val data: T) : ApiResult<T>()
    class Error<T>(val city: String = "") : ApiResult<T>()
    class Failure<T> : ApiResult<T>()
    class Loading<T> : ApiResult<T>()
}