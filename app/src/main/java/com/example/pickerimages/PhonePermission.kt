package com.example.pickerimages

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import android.Manifest
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PhonePermission {
    const val CALL_PHONE_PERMISSION_CODE = 1002

    fun checkCallPhonePermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCallPhonePermission(activity: Activity) {
        val shouldShowRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CALL_PHONE
            )
        if (shouldShowRationale) {
            showPermissionRationale(activity)
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_PHONE_PERMISSION_CODE
            )
        }
    }

    fun onRequestPermissionsResult(
        activity: Activity,
        requestCode: Int,
        grantResults: IntArray,
        onPermissionGranted: () -> Unit
    ) {
        if (requestCode == CALL_PHONE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                Toast.makeText(activity, "Call phone permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionRationale(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("Call Phone Permission Needed")
            .setMessage("This app requires phone call permission to make calls.")
            .setPositiveButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_PERMISSION_CODE
                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
