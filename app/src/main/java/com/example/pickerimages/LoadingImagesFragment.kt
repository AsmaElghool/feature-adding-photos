package com.example.pickerimages

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pickerimages.databinding.FragmentLoadingImagesBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoadingImagesFragment : Fragment() {
    private lateinit var binding: FragmentLoadingImagesBinding
    private val image1URL =
        "https://pbs.twimg.com/profile_images/931940051910582272/10UBvXCf_400x400.jpg"
    val maxRetries = 5

    private lateinit var firebaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoadingImagesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImagesWithShimmerEffect()
        loadImageWithGifAnimation()

        firebaseRef = FirebaseDatabase.getInstance().getReference("test")
        binding.firebaseBtn.setOnClickListener {
            saveData()
        }
        binding.retrieveBtn.setOnClickListener {
            tryErrorCodesHandlingFunction()
        }

    }


    private fun tryErrorCodesHandlingFunction() {

        val databaseHandler = ErrorCodesHandling()

        val databaseReference = FirebaseDatabase.getInstance().getReference("test")

        val errorCode = DatabaseError.WRITE_CANCELED.toString()
        val errorMessage = databaseHandler.convertRealtimeDatabaseErrorsToMessages(errorCode)

        Log.d("FirebaseDatabaseHandler", "Received error code: $errorMessage")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {

                        val contacts = childSnapshot.getValue(FirebaseData::class.java)

                        binding.nameTv.text = contacts?.name ?: ""
                        binding.phoneTv.text = contacts?.phoneNumber ?: ""
                    }
                } else {
                    handleNoDataError()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                databaseHandler.convertRealtimeDatabaseErrorsToMessages(error.code.toString())
            }
        })
    }

    private fun handleNoDataError() {
        Log.e("FirebaseDatabaseHandler", "No data found in the database.")
    }


    private fun saveData() {
        val name = binding.enterNameEt.text.toString()

        val concatId = firebaseRef.push().key!!
        val contacts = FirebaseData(concatId, name, "phone")

        firebaseRef.child(concatId).setValue(contacts)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireActivity(),
                        "Data stored successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireActivity(), "Data storage failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure and display an appropriate error message
                val errorMessage = when (exception) {
                    is DatabaseError -> {
                        // Handle specific DatabaseError types
                        when (exception.code) {
                            DatabaseError.WRITE_CANCELED -> "Write operation canceled"
                            DatabaseError.OPERATION_FAILED -> "Operation failed"
                            // Add more cases as needed
                            else -> "Unknown database error"
                        }
                    }

                    else -> "An unexpected error occurred: ${exception.message}"
                }

                Toast.makeText(requireActivity(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadImageWithGifAnimation() {
        Glide.with(this)
            .load(image1URL)
            .placeholder(R.drawable.animation)
            .thumbnail(Glide.with(this).load(R.drawable.animation))
            .dontAnimate()
            .into(binding.imageView3)
    }

    private fun loadImagesWithShimmerEffect() {
        // start shimmer animation
        binding.shimmerViewContainer.startShimmer()
        loadWithRetry(image1URL, binding.imageView1, binding.shimmerViewContainer, 0)

    }

    private fun loadWithRetry(
        imageUrl: String,
        imageView: ImageView,
        shimmerLayout: ShimmerFrameLayout,
        retryCount: Int
    ) {
        Glide.with(requireActivity())
            .load(imageUrl)
            .placeholder(R.color.gray_100)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    if (retryCount < maxRetries) {

                        Handler(Looper.getMainLooper()).postDelayed({
                            loadWithRetry(imageUrl, imageView, shimmerLayout, retryCount + 1)
                        }, 1000)
                    } else {
                        stopShimmerAndShowContent(shimmerLayout)
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    stopShimmerAndShowContent(shimmerLayout)
                    return false
                }
            })
            .into(imageView)
    }

    private fun stopShimmerAndShowContent(shimmerLayout: ShimmerFrameLayout) {
        Handler(Looper.getMainLooper()).post {
            shimmerLayout.stopShimmer()
            shimmerLayout.hideShimmer()
            shimmerLayout.background = null
            binding.frameLayout1.visibility = View.VISIBLE
        }
    }
}