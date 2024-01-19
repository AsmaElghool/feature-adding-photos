package com.example.pickerimages

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pickerimages.databinding.ActivityMainBinding
import com.example.pickerimages.databinding.BottomSheetItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var myRecyclerAdapter: RecyclerAdapter
    private lateinit var binding: ActivityMainBinding
    private var images: ArrayList<Uri?>? = null
    lateinit var bottomSheetBinding: BottomSheetItemBinding
    private var position = 0
    private var PICK_IMAGES_CODE = 0
    private var TAKE_PHOTO_CODE = 1
    lateinit var dialog: BottomSheetDialog

    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        images = ArrayList()

        binding.imageSwitcher.setFactory {
            ImageView(applicationContext)
        }

        //ActivityResultLauncher for camera result
        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageBitmap = result.data?.extras?.get("data") as Bitmap?
                    imageBitmap?.let {
                        val imageUri = getImageUriFromBitmap(it)
                        images!!.add(imageUri)
                        binding.imageSwitcher.setImageURI(imageUri)
                        position = images!!.size - 1
                        myRecyclerAdapter.notifyDataSetChanged()
                    } ?: run {
                        Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        binding.pickImagesBtn.setOnClickListener {
            showTakePhotoDailogOptions()
            bottomSheetBinding.btnGallery.setOnClickListener {
                pickImagesIntent()
                dialog.dismiss()
            }
            bottomSheetBinding.btnCamera.setOnClickListener {
                checkCameraPermission()
                dialog.dismiss()
            }
            dialog.show()
        }
        myRecyclerAdapter = RecyclerAdapter(images!!, findViewById(android.R.id.content))
        binding.recyclerView.adapter = myRecyclerAdapter
    }

    fun showTakePhotoDailogOptions() {
        dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_item)
        bottomSheetBinding = BottomSheetItemBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
    }

    private fun pickImagesIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            // Check if the user has denied the permission before
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                showPermissionRationale()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    TAKE_PHOTO_CODE
                )
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoLauncher.launch(cameraIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == TAKE_PHOTO_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionRationale() {
        //explain why the permission is needed
        AlertDialog.Builder(this)
            .setTitle("Camera Permission Needed")
            .setMessage("This app requires camera permission to take photos.")
            .setPositiveButton("OK") { _, _ ->
                // Request the camera permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    TAKE_PHOTO_CODE
                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_CODE && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                // pick multiple images
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    images!!.add(imageUri)
                }
                binding.imageSwitcher.setImageURI(images!!.last())
                position = images!!.size - 1
                myRecyclerAdapter.notifyDataSetChanged()

            } else if (data?.data != null) {
                // pick a single image
                val imageUri = data.data
                images!!.add(imageUri)

                binding.imageSwitcher.setImageURI(images!!.last())
                position = images!!.size - 1

                myRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }
}