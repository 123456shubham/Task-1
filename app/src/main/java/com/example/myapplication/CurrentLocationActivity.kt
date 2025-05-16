package com.example.myapplication


import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.util.Printer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.api.ApiResponse
import com.example.myapplication.api.RetrofitBuilder
import com.example.myapplication.databinding.ActivityCurrentLocationBinding
import com.example.myapplication.factory.GithubFactory
import com.example.myapplication.model.CityRequest
import com.example.myapplication.model.Data
import com.example.myapplication.repository.GithubRepository
import com.example.myapplication.viewModel.GithubViewModel
import com.google.android.gms.location.*
import java.util.Locale

class CurrentLocationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCurrentLocationBinding.inflate(layoutInflater) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
     var cityName: String=""

    private  var checkValue: Boolean=false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 101
    }

    private val githubRepository by lazy {
        GithubRepository(RetrofitBuilder.getInstance(application)!!.api, application)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, GithubFactory(githubRepository))[GithubViewModel::class.java]
    }


    private lateinit var locationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (hasLocationPermission()) {
            locationClient = LocationServices.getFusedLocationProviderClient(this@CurrentLocationActivity)
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }

        init()
    }

    fun init() {

        viewModel.launchCityLiveData.observe(this) {
            when (it) {

                is ApiResponse.Loading -> {
                    // show progress bar
                }

                is ApiResponse.Error -> {
                    Toast.makeText(this@CurrentLocationActivity, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is ApiResponse.Success -> {

                    it.data?.let { response ->

                        for (name in response.data ?: emptyList()) {
                            val cityNameValue = name?.city_name
                                if (cityName.toUpperCase().equals(cityNameValue)) {
                                    checkValue=true
                                    break
                                }
                            }

                        if (checkValue==true){
                            binding.locationText.text = "True"

                        }else{
                            binding.locationText.text = "False"
                        }

                        Log.d("checkValue","checkValue =${cityName}")
                        Log.d("checkValue","cityName =${checkValue}")
                        Log.d("checkValue","data =${  it.data.toString()}")

                    }


                }
            }

        }

        viewModel.launchCity(CityRequest(6493))



    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }


    private fun getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is not granted, request it from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
            return
        }

        // Fetch the last known location
        locationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude

                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    var cName = addresses[0].locality
//                    cityName= CommonFunction.getAddressFromLocation(lat,lon)
                    Toast.makeText(this,"$cName", Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::locationCallback.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}