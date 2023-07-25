package com.aditya.placesinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.aditya.placesinfo.databinding.ActivityAutocompleteBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.ktx.widget.PlaceSelectionError
import com.google.android.libraries.places.ktx.widget.PlaceSelectionSuccess
import com.google.android.libraries.places.ktx.widget.placeSelectionEvents
import com.google.android.libraries.places.widget.AutocompleteSupportFragment

private const val TAG = "AutocompleteActivity"

class AutocompleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAutocompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutocompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val placeFragment = supportFragmentManager.findFragmentById(R.id.fragment)
                    as AutocompleteSupportFragment

        placeFragment.setPlaceFields(listOf(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.TYPES))

        lifecycleScope.launchWhenCreated {
            placeFragment.placeSelectionEvents().collect { placeEvent ->
                when (placeEvent) {
                    is PlaceSelectionSuccess -> binding.response.text = placeEvent.place.toString()

                    is PlaceSelectionError -> Toast.makeText(
                        this@AutocompleteActivity,
                        "Failed to get place '${placeEvent.status.statusMessage}'",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}