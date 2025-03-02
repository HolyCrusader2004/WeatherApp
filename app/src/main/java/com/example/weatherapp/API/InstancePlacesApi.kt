package com.example.weatherapp.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InstancePlacesApi {
    private const val baseUrl = "https://maps.googleapis.com/"

    private fun getInstance() : Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val placesApi : GooglePlacesApi = getInstance().create(GooglePlacesApi::class.java)
}