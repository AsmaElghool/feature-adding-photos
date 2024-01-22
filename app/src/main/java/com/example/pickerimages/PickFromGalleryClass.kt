package com.example.pickerimages

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

class PickFromGalleryClass(
    private val activity: FragmentActivity,
    private val onImagesPicked: (List<Uri>) -> Unit
) {
    private lateinit var pickImagesLauncher: ActivityResultLauncher<Intent>

    init {
        initialize()
    }

    private fun initialize() {
        pickImagesLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handlePickImagesResult(result)
            }
    }

    fun pickImages() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        pickImagesLauncher.launch(Intent.createChooser(intent, "Select Image(s)"))
    }

    private fun handlePickImagesResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data?.clipData != null) {
                // pick multiple images
                val imagesList = mutableListOf<Uri>()
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    imagesList.add(imageUri)
                }
                onImagesPicked(imagesList)
            } else if (data?.data != null) {
                // pick a single image
                val imageUri = data.data
                if (imageUri != null) {
                    onImagesPicked(listOf(imageUri))
                }
            }
        }
    }
}