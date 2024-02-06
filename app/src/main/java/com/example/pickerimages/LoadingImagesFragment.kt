package com.example.pickerimages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pickerimages.databinding.FragmentLoadingImagesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException


class LoadingImagesFragment : Fragment() {
    private lateinit var binding: FragmentLoadingImagesBinding
    private val image1URL = "https://pbs.twimg.com/profile_images/931940051910582272/10UBvXCf_400x400.jpg"
    private lateinit var firebaseRef: DatabaseReference
    private val databaseReference = FirebaseDatabase.getInstance().getReference("test")
    private val storageRef = FirebaseStorage.getInstance().getReference("/profilePictures")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadingImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImageWithGifAnimation()

        firebaseRef = FirebaseDatabase.getInstance().getReference("test")

        binding.retrieveBtn.setOnClickListener {

            simulateStorageOperation()
            tryErrorCodesHandlingFunction()
        }
    }

    private fun simulateStorageOperation() {
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            Log.e("operation success", "done successfully")
        }.addOnFailureListener { exception ->
            Log.e("error", "Error code: ${exception.javaClass}")

            val errorCode = (exception as StorageException).errorCode
            Log.e("error", "Error code: $errorCode")
            val databaseHandler = ErrorCodesHandling()
            val errorMessage = databaseHandler.convertStorageErrorsToMessages(errorCode)
            Log.e("error message", "Error code: $errorMessage")
        }
    }

    private fun tryErrorCodesHandlingFunction() {

        val databaseHandler = ErrorCodesHandling()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val contacts = childSnapshot.getValue(FirebaseData::class.java)

                        binding.nameTv.text = contacts?.name ?: ""
                        binding.phoneTv.text = contacts?.phoneNumber ?: ""
                    }
                } else {
                    Log.d("FirebaseDatabaseHandler", "no data found")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                val errorCode=error.code
                Log.d("error code",errorCode.toString())

                val errorMessage=databaseHandler.convertRealtimeDatabaseErrorsToMessages(error.code)
                Log.d("error message",errorMessage)
            }
        })
    }
    private fun loadImageWithGifAnimation() {
        Glide.with(this)
            .load(image1URL)
            .placeholder(R.drawable.animation)
            .thumbnail(Glide.with(this).load(R.drawable.animation))
            .dontAnimate()
            .into(binding.imageView3)
    }
}