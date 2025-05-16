package com.example.myapplication


import android.Manifest

import android.content.Context

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


import java.util.Locale

object CommonFunction {

    private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

    fun getCurrentLocation(
        context: Context,
        onLocationResult: (Location?) -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionDenied()
            return
        }

        // Request location updates
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            onLocationResult(location)
        }.addOnFailureListener {
            onLocationResult(null)
        }
    }

    // Function to convert latitude and longitude to address
    fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Unable to get address"
        }

        if (addresses.isNullOrEmpty()) {
            return "Address not found"
        }

        val address: Address = addresses[0]
        val cityName = address.locality
        val stateName = address.adminArea
        val countryName = address.countryName
        val postalCode = address.postalCode
        val addressLines = ArrayList<String>()

        for (i in 0..address.maxAddressLineIndex) {
            addressLines.add(address.getAddressLine(i))
        }

        return cityName
    }





}