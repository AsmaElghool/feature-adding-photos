package com.example.pickerimages

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.pickerimages.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var myRecyclerAdapter: RecyclerAdapter
    private lateinit var binding: ActivityMainBinding
    private var images: ArrayList<Uri?>? = null

    private var position = 0
    private var PICK_IMAGES_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        images = ArrayList()

        binding.imageSwitcher.setFactory {
            ImageView(applicationContext)
        }

        binding.pickImagesBtn.setOnClickListener {
            pickImagesIntent()
        }

        myRecyclerAdapter = RecyclerAdapter(images!!, findViewById(android.R.id.content))
        binding.recyclerView.adapter = myRecyclerAdapter
    }

    private fun pickImagesIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)

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