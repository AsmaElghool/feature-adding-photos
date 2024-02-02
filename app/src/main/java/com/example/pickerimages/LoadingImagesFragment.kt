package com.example.pickerimages

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pickerimages.databinding.FragmentLoadingImagesBinding
import com.facebook.shimmer.ShimmerFrameLayout


class LoadingImagesFragment : Fragment() {
    private lateinit var binding: FragmentLoadingImagesBinding
    private val image1URL =
        "https://pbs.twimg.com/profile_images/931940051910582272/10UBvXCf_400x400.jpg"
    val maxRetries = 5


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