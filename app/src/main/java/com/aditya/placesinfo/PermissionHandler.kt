package com.aditya.placesinfo

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val TAG = "PermissionHandler"

class PermissionHandler {

    fun requestPermissions(
        context: Activity,
        permissions: Array<String>,
        launcher: ActivityResultLauncher<Array<String>>,
        message: String
    ) {
        val permissionsToRequest = mutableListOf<String>()
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "Permission Needed: $permission")
                permissionsToRequest += permission
            }
            if (permissionsToRequest.isEmpty()) {
                Log.i(TAG, "All permissions granted")
            } else {
                Log.w(TAG, "All permissions not granted yet!")
                val shouldShoeRationale = permissionsToRequest.any {
                    ActivityCompat.shouldShowRequestPermissionRationale(context, it)
                }
                if (shouldShoeRationale) {
                    Log.i(TAG, "Permissions {} not granted yet!, showing rationale")
                    showAlertDialogue(
                        context,
                        permissionsToRequest.toTypedArray(),
                        launcher,
                        message
                    )
                } else {
                    Log.i(TAG, "Asking for permissions first time")
                    launcher.launch(permissionsToRequest.toTypedArray())
                }
            }
        }
    }

    private fun showAlertDialogue(
        context: Context,
        permissions: Array<String>,
        launcher: ActivityResultLauncher<Array<String>>,
        message: String
    ) {
        AlertDialog.Builder(context).apply {
            setTitle("For the current location tracking, LOCATION permission is needed, otherwise app might malfunction.")
            setPositiveButton("Grant") { _, _ ->
                launcher.launch(permissions)
            }
                .setNegativeButton("No") { _, _ ->
                    Log.i(TAG, "Permission: $permissions denied from alert box.")
                }
        }.create().show()
    }
}