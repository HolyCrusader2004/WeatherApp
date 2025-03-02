package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.API.Instance
import com.example.weatherapp.data.NetworkResponse
import com.example.weatherapp.data.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {
    private val weatherApi = Instance.weatherApi
    private val apiKey = "8af4307f347b467aa6381230252302"
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(location: String){
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeatherData(apiKey, location)
                when(response.isSuccessful){
                    true -> {
                        response.body()?.let {
                            _weatherResult.value = NetworkResponse.Success(it)
                            Log.d("response", _weatherResult.value.toString())
                        }
                    }
                    false -> {
                        _weatherResult.value = NetworkResponse.Error("failed to load")
                    }
                }
            }catch (e: Exception){
                Log.d("eroare", e.message.toString())
                _weatherResult.value = NetworkResponse.Error(e.message.toString())
            }
        }
    }
}