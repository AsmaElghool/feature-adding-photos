package com.example.pickerimages

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CaptureWithCameraClass(
    private val activity: AppCompatActivity,
    private val onImageCaptured: (String) -> Unit
) {
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>
    init {
        initialize()
    }

    private fun initialize() {
        takePhotoLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleCameraResult(result)
            }
    }

    fun openCamera() {
        if (CameraPermission.checkCameraPermission(activity)) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoLauncher.launch(cameraIntent)
        } else {
            CameraPermission.requestCameraPermission(activity)
        }
    }

    private fun handleCameraResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                val imageUri = saveBitmapToMediaStore(it)
                imageUri?.let {
                    onImageCaptured(it.toString())
                } ?: run {
                    Toast.makeText(
                        activity,
                        "Failed to save image to MediaStore",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
                Toast.makeText(activity, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveBitmapToMediaStore(bitmap: Bitmap): Uri? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmm ss", Locale.getDefault()).format(Date())
        val displayName = "JPEG_${timeStamp}_"
        val mimeType = "image/jpeg"
        val contentResolver = activity.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        }
        val imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                currentPhotoPath = getImagePathFromUri(uri)
                return uri
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(activity, "Failed to save image to MediaStore", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return null
    }

    private fun getImagePathFromUri(uri: Uri?): String? {
        if (uri == null) return null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return it.getString(columnIndex)
        }
        return null
    }

    companion object {
        var currentPhotoPath: String? = null
    }
}