package com.example.screensage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screensage.R
import com.example.screensage.databinding.FragmentMediaDetailedBinding
import com.example.screensage.entities.MediaDetailed
import com.example.screensage.network.ReviewRequest
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.MediaAdapter
import com.example.screensage.utils.ReviewAdapter
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.launch

class MediaDetailedFragment : Fragment() {
    private var _binding: FragmentMediaDetailedBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mediaId: Int? = null
    private var mediaType: String? = null

    private lateinit var mediaPoster: ImageView
    private lateinit var mediaTitle: TextView
    private lateinit var mediaRatingBar: RatingBar
    private lateinit var mediaOverview: TextView

    private lateinit var mediaExtraInfo: TextView
    private lateinit var mediaGenres: TextView

    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private var totalPages = 1
    private var currentPage = 1

    private lateinit var reviewRatingBarInput: RatingBar
    private lateinit var reviewContentInput: EditText
    private lateinit var reviewSubmitButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mediaId = it.getInt("mediaId")
            mediaType = it.getString("mediaType")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaDetailedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPoster = view.findViewById(R.id.mediaPoster)
        mediaTitle = view.findViewById(R.id.mediaTitle)
        mediaRatingBar = view.findViewById(R.id.mediaRatingBar)
        mediaOverview = view.findViewById(R.id.mediaOverview)

        mediaExtraInfo = view.findViewById(R.id.mediaExtraInfo)
        mediaGenres = view.findViewById(R.id.mediaGenres)

        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView)
        reviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        reviewAdapter = ReviewAdapter(mutableListOf())
        reviewRecyclerView.adapter = reviewAdapter

        reviewRatingBarInput = view.findViewById(R.id.reviewRatingBarInput)
        reviewContentInput = view.findViewById(R.id.reviewContentInput)
        reviewSubmitButton = view.findViewById(R.id.reviewSubmitButton)

        reviewSubmitButton.setOnClickListener {
            submitReview()
        }

        fetchMediaDetails()
        fetchReviews(currentPage)

        binding.btnNextReviews.setOnClickListener { goToNextPage() }
        binding.btnPreviousReviews.setOnClickListener { goToPreviousPage() }
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

    private fun fetchReviews(page: Int) {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getMediaReviewsById(
                    "Bearer $token",
                    mediaType ?: "movies",
                    mediaId ?: 0,
                    page
                )
                if (response.isSuccessful) {
                    response.body()?.let { reviewResponse ->
                        reviewAdapter.updateReviews(reviewResponse.content)
                        totalPages = reviewResponse.totalPages
                    }
                    updateButtonVisibility()
                } else {
                    val errorMessage = ErrorUtil.parseApiErrorMessage(response)
                    ToastUtil.showToast(requireContext(), errorMessage)
                }
            } catch (e: Exception) {
                ToastUtil.showToast(requireContext(), "Network error: ${e.message}")
            }
        }
    }

    private fun submitReview() {
        val token = AuthManager.getToken(requireContext()) ?: return

        val rating = reviewRatingBarInput.rating.toDouble()
        val content = reviewContentInput.text.toString().trim()

        if (content.isEmpty()) {
            ToastUtil.showToast(requireContext(), "Review content cannot be empty!")
            return
        }

        val reviewRequest = ReviewRequest(rating, content)

        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.postMediaReview(
                    "Bearer $token",
                    mediaType ?: "movies",
                    mediaId ?: 0,
                    reviewRequest
                )

                if (response.isSuccessful) {
                    val newReview = response.body()
                    newReview?.let {
                        reviewAdapter.updateReviews(listOf(it))
                        reviewContentInput.text.clear()
                        reviewRatingBarInput.rating = 0f
                        ToastUtil.showToast(requireContext(), "Review submitted!")
                    }
                } else {
                    val errorMessage = ErrorUtil.parseApiErrorMessage(response)
                    ToastUtil.showToast(requireContext(), errorMessage)
                }
            } catch (e: Exception) {
                ToastUtil.showToast(requireContext(), "Network error: ${e.message}")
            }
        }
    }

    private fun updateUI(media: MediaDetailed) {
        mediaTitle.text = media.title ?: media.name ?: "No title"
        mediaOverview.text = media.overview ?: "No Overview"
        mediaRatingBar.rating = (media.averageRating ?: 0.0).toFloat()

        mediaGenres.text = media.genres?.joinToString(", ") { it.name } ?: "No genres"
        mediaExtraInfo.text = when (mediaType) {
            "movies" -> media.runtime?.let { "${it} min" } ?: ""
            "shows" -> media.numberOfEpisodes?.let { "$it episodes" } ?: ""
            else -> ""
        }

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${media.posterPath}")
            .into(mediaPoster)
    }

    private fun goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchReviews(currentPage)
        }
    }

    private fun goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchReviews(currentPage)
        }
    }

    private fun updateButtonVisibility() {
        binding.btnPreviousReviews.visibility = if (currentPage > 1) View.VISIBLE else View.GONE
        binding.btnPreviousReviews.isEnabled = currentPage > 1

        binding.btnNextReviews.visibility = if (currentPage < totalPages) View.VISIBLE else View.GONE
        binding.btnNextReviews.isEnabled = currentPage < totalPages
    }
}
