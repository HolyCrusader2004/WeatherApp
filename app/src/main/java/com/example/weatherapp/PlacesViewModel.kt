package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.GooglePredictionResponse
import com.example.weatherapp.API.InstancePlacesApi
import com.example.weatherapp.data.NetworkResponse
import kotlinx.coroutines.launch

class PlacesViewModel: ViewModel(){
    private val placesApi = InstancePlacesApi.placesApi
    private val _placesResult = MutableLiveData<NetworkResponse<GooglePredictionResponse>>()
    val placesResult: LiveData<NetworkResponse<GooglePredictionResponse>> = _placesResult

    fun getPlacesData(location: String) {
        viewModelScope.launch {
            try {
                val response = placesApi.getPredictions(input = location)
                _placesResult.value = NetworkResponse.Success(response)
                Log.d("PlacesViewModel", "Success: $response")
            } catch (e: Exception) {
                Log.d("PlacesViewModel", "Exception: ${e.message}")
                _placesResult.value = NetworkResponse.Error(e.message.toString())
            }
        }
    }
    fun clearPlacesData() {
        _placesResult.value = NetworkResponse.Success(GooglePredictionResponse(ArrayList()))  // Or set it to your default state
    }

}
