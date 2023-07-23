package com.aditya.placesinfo

import android.app.Activity
import androidx.annotation.StringRes

enum class PlacesList(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val activity: Class<out Activity>
) {

    DETAILS_FRAGMENT_DEMO(
        R.string.details_demo_title,
        R.string.details_demo_description,
        DetailsActivity::class.java
    ),

    CURRENT_FRAGMENT_DEMO(
        R.string.current_demo_title,
        R.string.current_demo_description,
        CurrentActivity::class.java
    )

}