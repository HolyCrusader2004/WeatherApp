package com.example.weatherapp.API

import com.example.weatherapp.data.GooglePredictionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {
    @GET("maps/api/place/autocomplete/json")
    suspend fun getPredictions(
        @Query("key") key: String ="YOUR_API_KEY",
        @Query("types") types: String = "address",
        @Query("input") input: String
    ): GooglePredictionResponse
}
