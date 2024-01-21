package com.example.pickerimages

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.pickerimages.databinding.ActivityMainBinding
import com.example.pickerimages.databinding.BottomSheetItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    private lateinit var myRecyclerAdapter: RecyclerAdapter
    private lateinit var binding: ActivityMainBinding
    private var images: ArrayList<Uri?>? = null
    private lateinit var bottomSheetBinding: BottomSheetItemBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var takeImageByCamera: CaptureWithCameraClass
    private lateinit var takeByImagePicker: PickFromGalleryClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        images = ArrayList()

        binding.imageSwitcher.setFactory {
            ImageView(applicationContext)
        }

        takeImageByCamera = CaptureWithCameraClass(this) { imageUri ->
            updateImageList(Uri.parse(imageUri))
        }

        takeByImagePicker = PickFromGalleryClass(this) { imagesList ->
            for (imageUri in imagesList) {
                updateImageList(imageUri)
            }
        }

        binding.pickImagesBtn.setOnClickListener {
            showTakePhotoDailogOptions()
            bottomSheetBinding.btnGallery.setOnClickListener {
                takeByImagePicker.pickImages()
                dialog.dismiss()
            }
            bottomSheetBinding.btnCamera.setOnClickListener {
                takeImageByCamera.openCamera()
                dialog.dismiss()
            }
            dialog.show()
        }

        myRecyclerAdapter = RecyclerAdapter(images!!, findViewById(android.R.id.content))
        binding.recyclerView.adapter = myRecyclerAdapter
    }

    private fun showTakePhotoDailogOptions() {
        dialog = BottomSheetDialog(this)
        bottomSheetBinding = BottomSheetItemBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
    }

    private fun updateImageList(imageUri: Uri) {
        images!!.add(imageUri)
        binding.imageSwitcher.setImageURI(imageUri)
        myRecyclerAdapter.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CameraPermission.onRequestPermissionsResult(this, requestCode, grantResults) {
            //when permission is granted
            handleCameraClick()
        }
    }

    private fun handleCameraClick() {
        takeImageByCamera.openCamera()
    }
}
