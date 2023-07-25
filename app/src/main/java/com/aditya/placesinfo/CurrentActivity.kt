package com.aditya.placesinfo

import android.Manifest
import android.Manifest.permission
import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.aditya.placesinfo.databinding.ActivityCurrentBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.ktx.api.net.awaitFindCurrentPlace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

private const val TAG = "CurrentActivity"

class CurrentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCurrentBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var currentPlace: Button
    private lateinit var responseView: TextView
    private lateinit var permissioner: PermissionHandler

    private val permissions = arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            handlePermissionsResult(permissions)
        }

    private fun handlePermissionsResult(permissions: Map<String, Boolean>) {
        for ( (permission, isGranted ) in permissions ) {
            when (isGranted) {
                true -> {
                    Log.i(TAG, "result received -> PermissionGranted: $permission")
                }
                false -> {
                    Log.i(TAG, "result received -> Permission denied $permission")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        placesClient = Places.createClient(this)
        currentPlace = binding.currentButton
        responseView = binding.currentResponseContent
        permissioner = PermissionHandler()
//        responseView.setTextIsSelectable(true)
//        responseView.isEnabled = false
//        responseView.isEnabled = true

        currentPlace.setOnClickListener {
            checkPermissionThenFindCurrentPlace()
        }

    }

    private fun checkPermissionThenFindCurrentPlace() {
        if((ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
          findCurrentPlace()
        }
        else {
            permissioner.requestPermissions(this, permissions, permissionLauncher, "Location permission is needed for proper functioning of app, other this app may misbehave")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresPermission(anyOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    private fun findCurrentPlace() {
        Log.i(TAG, "findCurrentPlace: Started ")
        val placeFields: List<Place.Field> =
            listOf(Place.Field.NAME, Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.TYPES)

        // Use the builder to create a FindCurrentPlaceRequest.
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)
        lifecycleScope.launch {
          if( (ActivityCompat.checkSelfPermission(
                    this@CurrentActivity,
                    ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) || (ActivityCompat.checkSelfPermission(
                    this@CurrentActivity,
                    ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
          ) {
              try {

                  val response: FindCurrentPlaceResponse = placesClient.awaitFindCurrentPlace(placeFields)
                  responseView.text = response.toString()
                  val l = response.placeLikelihoods.forEach {
                      val x = it.place.latLng?.latitude
                      val y = it.place.latLng?.longitude
                      val type = it.place.types?.forEach {
                                it.name
                      }
                  }
                  Log.i(TAG, "response: $response ")
//                  val movementMethod = ScrollingMovementMethod()
//                  responseView.movementMethod = movementMethod
              } catch (ex: Exception) {
                  Log.w(TAG, "Exception while getting response $ex" )
                  responseView.text = ex.message
              }
          } else {
              Log.e(TAG, "Permission not granted for location access.", )
              return@launch
          }
          }

        }
}