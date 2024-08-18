package com.crm.edu.utils

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(
    onLocationAvailable: (Location?) -> Unit,
) {

    val context = LocalContext.current
    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    LaunchedEffect(locationPermissionState) {
        if (locationPermissionState.status.isGranted) {
            getLastKnownLocation(fusedLocationClient, onLocationAvailable)
        }else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            locationPermissionState.status.isGranted -> {
                // Permission is granted
                Text("Location permission granted")
            }

            locationPermissionState.status.shouldShowRationale -> {
                Text("Location permission is needed to access your current location")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }

            !locationPermissionState.status.isGranted -> {
                Text("Location permission required")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Request Permission")
                }
            }
        }

    }

}

@SuppressLint("MissingPermission")
fun getLastKnownLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationAvailable: (Location?) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        onLocationAvailable(location)
    }
}
