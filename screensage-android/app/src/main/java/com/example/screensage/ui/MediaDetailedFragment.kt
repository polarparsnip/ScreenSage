package com.example.screensage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.screensage.R
import com.example.screensage.entities.MediaDetailed
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.launch

class MediaDetailedFragment : Fragment() {
    private var mediaId: Int? = null
    private var mediaType: String? = null

    private lateinit var mediaPoster: ImageView
    private lateinit var mediaTitle: TextView
    private lateinit var mediaRatingBar: RatingBar
    private lateinit var mediaOverview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mediaId = it.getInt("mediaId")
            mediaType = it.getString("mediaType")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_media_detailed, container, false)

        mediaPoster = view.findViewById(R.id.mediaPoster)
        mediaTitle = view.findViewById(R.id.mediaTitle)
        mediaRatingBar = view.findViewById(R.id.mediaRatingBar)
        mediaOverview = view.findViewById(R.id.mediaOverview)

        // val mediaId = arguments?.getInt("mediaId") ?: return view
        fetchMediaDetails()

        return view
    }

    private fun fetchMediaDetails() {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getMediaById(
                    "Bearer ${token}",
                    mediaType ?: "movies",
                    mediaId ?: 0
                )
                if (response.isSuccessful) {
                    response.body()?.let { media ->
                        updateUI(media)
                    }
                } else {
                    val errorMessage = ErrorUtil.parseApiErrorMessage(response)
                    println(errorMessage)
                    ToastUtil.showToast(requireContext(), errorMessage)
                }
            } catch (e: Exception) {
                println("Network error: ${e.message}")
                ToastUtil.showToast(requireContext(), "Network error: ${e.message}")
            }
        }
    }

    private fun updateUI(media: MediaDetailed) {
        mediaTitle.text = media.title ?: media.name ?: "No title"
        mediaOverview.text = media.overview ?: "No Overview"
        mediaRatingBar.rating = (media.averageRating ?: 0.0).toFloat()

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${media.posterPath}")
            .into(mediaPoster)
    }
}
