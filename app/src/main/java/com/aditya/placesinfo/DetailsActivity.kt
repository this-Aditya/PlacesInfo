package com.aditya.placesinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.aditya.placesinfo.databinding.ActivityDetailsBinding
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.ktx.api.net.awaitFetchPlace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "DetailsActivity"

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var placesClient: PlacesClient

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        placesClient = Places.createClient(this)


        binding.detailsButton.setOnClickListener {
            val placeId = binding.detailsInput.text.toString()
            val placeField = listOf(
                    Place.Field.NAME,
                    Place.Field.ID,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS,
                    Place.Field.TYPES
            )
            lifecycleScope.launch {
                try {
                    val response: FetchPlaceResponse = placesClient.awaitFetchPlace(placeId, placeField)
                    binding.detailsResponseContent.text = response.toString()
                    Log.i(TAG, "Response received: \n $response")
                } catch (ex: Exception) {
                    Log.w(TAG, "Exception while fetching the place: $ex")
                    binding.detailsResponseContent.text = ex.message.toString()
                }
            }
        }

    }
}

