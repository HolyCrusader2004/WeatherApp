package com.example.weatherapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.data.NetworkResponse
import com.example.weatherapp.data.WeatherModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel, placesViewModel: PlacesViewModel){
    var location by remember {
        mutableStateOf("")
    }
    var showWelcomeScreen by remember {
        mutableStateOf(true)
    }

    val result = viewModel.weatherResult.observeAsState()

    val context = LocalContext.current

    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
           SearchBar(onPlaceSelected = {
               location = it
               viewModel.getData(location)
               showWelcomeScreen = false
           }, placesViewModel = placesViewModel)
        }
        if (showWelcomeScreen){
            WelcomeScreen()
        }else {
            when (val apiresult = result.value) {
                is NetworkResponse.Error -> {
                    Toast.makeText(context, apiresult.message, Toast.LENGTH_LONG).show()
                }

                is NetworkResponse.Success -> {
                    WeatherDetail(data = apiresult.data)
                }

                is NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                else -> {}
            }
        }
    }
}

@Composable
fun WeatherDetail(data: WeatherModel){
    Column (
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ){
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "location",
                modifier = Modifier.size(40.dp))
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "${data.current.temp_c} °C", fontSize = 56.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        AsyncImage(model = "https:${data.current.condition.icon}".replace("64x64", "128x128"), contentDescription = "icon",
            modifier = Modifier.size(160.dp))
        Text(text = data.current.condition.text, fontSize = 20.sp, textAlign = TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
                ){
                    UiKeyVal(key = "Humidity", value = data.current.humidity)
                    UiKeyVal(key = "Wind Speed (kph)", value = data.current.wind_kph)
                }
                Row (
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
                ){
                    UiKeyVal(key = "Gust Speed (kph)", value = data.current.gust_kph)
                    UiKeyVal(key = "Local Time", value = data.location.localtime)
                }
            }
        }

    }
}

@Composable
fun UiKeyVal(key: String, value: String){
    Column (modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to Weather App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))

        ) {
            Image(
                painter = painterResource(id = R.drawable.weatherapp),
                contentDescription = "Weather App Logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Search for a city to see the current weather",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}
