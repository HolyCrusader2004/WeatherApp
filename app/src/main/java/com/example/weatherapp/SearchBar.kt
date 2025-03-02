package com.example.weatherapp

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.NetworkResponse

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onPlaceSelected: (String) -> Unit,
    placesViewModel: PlacesViewModel
) {
    var location by remember { mutableStateOf("") }
    val placesResult = placesViewModel.placesResult.observeAsState()
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = modifier) {
        OutlinedTextField(
            value = location,
            onValueChange = { newText ->
                location = newText
                isDropdownExpanded = false
            },
            label = { Text(text = "Search for any location") },
            trailingIcon = {
                IconButton(onClick = {
                    if (location.isNotBlank()) {
                        placesViewModel.getPlacesData(location)
                        isDropdownExpanded = true
                    }
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            }
            ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        when (val apiResult = placesResult.value) {
            is NetworkResponse.Error -> {
                Toast.makeText(context, apiResult.message, Toast.LENGTH_LONG).show()
            }
            is NetworkResponse.Success -> {
                if (apiResult.data.predictions.isNotEmpty() && isDropdownExpanded) {
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        apiResult.data.predictions.forEach { place ->
                            DropdownMenuItem(
                                text = { Text(text = place.description) },
                                onClick = {
                                    onPlaceSelected(place.description)
                                    location = ""
                                    placesViewModel.clearPlacesData()
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }else if (apiResult.data.predictions.isEmpty() && isDropdownExpanded){
                    isDropdownExpanded = false
                    Toast.makeText(context, "No place found", Toast.LENGTH_LONG).show()
                }
            }
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            else -> {}
        }
    }
}
