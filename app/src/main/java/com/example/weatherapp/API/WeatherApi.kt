package com.example.weatherapp.API

import com.example.weatherapp.data.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/v1/current.json")
    suspend fun getWeatherData(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): Response<WeatherModel>
}