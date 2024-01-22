package com.example.pickerimages

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.pickerimages.databinding.BottomSheetItemBinding
import com.example.pickerimages.databinding.FragmentShowImagesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowImagesFragment : Fragment() {


    private lateinit var myRecyclerAdapter: RecyclerAdapter
    private lateinit var binding: FragmentShowImagesBinding
    private var images: ArrayList<Uri?>? = null
    private lateinit var bottomSheetBinding: BottomSheetItemBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var takeImageByCamera: CaptureWithCameraClass
    private lateinit var takeByImagePicker: PickFromGalleryClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        images = ArrayList()

        binding.imageSwitcher.setFactory {
            ImageView(requireActivity())
        }

        takeImageByCamera = CaptureWithCameraClass(requireActivity()) { imageUri ->
            updateImageList(Uri.parse(imageUri))
        }

        takeByImagePicker = PickFromGalleryClass(requireActivity()) { imagesList ->
            for (imageUri in imagesList) {
                updateImageList(imageUri)
            }
        }

        myRecyclerAdapter = RecyclerAdapter(images!!, view)
        binding.recyclerView.adapter = myRecyclerAdapter

        binding.pickImagesBtn.setOnClickListener {
            showTakePhotoDialogOptions()
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
        binding.toSecondFragment.setOnClickListener {
            findNavController().navigate(R.id.action_showImagesFragment_to_favoriteFragment)
        }
    }

    private fun showTakePhotoDialogOptions() {
        dialog = BottomSheetDialog(requireContext())
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
        CameraPermission.onRequestPermissionsResult(requireActivity(), requestCode, grantResults) {
            // when permission granted
            handleCameraClick()
        }
    }

    private fun handleCameraClick() {
        takeImageByCamera.openCamera()
    }
}