package com.crm.edu.utils

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority

@SuppressLint("MissingPermission")
fun getLastKnownLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationAvailable: (Location?) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        Log.d("EduLogs", "Last Location: ${location.latitude}, ${location.longitude}")
        onLocationAvailable(location)
    }
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationAvailable: (Location?) -> Unit
) {
    val currentLocationRequest = CurrentLocationRequest.Builder()
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY) // Or other priority
        .setDurationMillis(5000) // Optional: Set a timeout
        .setMaxUpdateAgeMillis(0) // Optional: Request the most recent location
        .build()

    fusedLocationClient.getCurrentLocation(currentLocationRequest, null)
        .addOnSuccessListener { location ->
            location?.let {
                Log.d("EduLogs", "Current Location: ${it.latitude}, ${it.longitude}")
                onLocationAvailable(it)
            }
        }
}
