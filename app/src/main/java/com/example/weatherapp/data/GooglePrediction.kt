package com.example.weatherapp.data

data class GooglePrediction(
    val description: String,
    val terms: List<GooglePredictionTerm>
)
