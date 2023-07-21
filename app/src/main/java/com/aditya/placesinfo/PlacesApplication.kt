package com.aditya.placesinfo

import android.app.Application
import com.google.android.libraries.places.api.Places

class PlacesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initialize(this, BuildConfig.GOOGLE_MAPS_API_KEY)
    }
}