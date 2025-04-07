package com.example.screensage.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screensage.R
import com.example.screensage.databinding.FragmentMediaDetailedBinding
import com.example.screensage.entities.MediaDetailed
import com.example.screensage.entities.MediaList
import com.example.screensage.network.MediaListItemRequest
import com.example.screensage.network.MediaListRequest
import com.example.screensage.network.ReviewRequest
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.ReviewAdapter
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Locale

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

    private lateinit var likeButton: ImageView
    private lateinit var likeCountTextView: TextView
    private var isLiked = false
    private var likeCount = 0

    private lateinit var mediaPosterImg: String
    private lateinit var mediaListSpinner: Spinner
    private var userMediaLists: List<MediaList> = listOf()
    private lateinit var watchlistSpinner: Spinner
    private var userWatchlists: List<MediaList> = listOf()

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

        reviewAdapter = ReviewAdapter(mutableListOf(), false)
        reviewRecyclerView.adapter = reviewAdapter

        reviewRatingBarInput = view.findViewById(R.id.reviewRatingBarInput)
        reviewContentInput = view.findViewById(R.id.reviewContentInput)
        reviewSubmitButton = view.findViewById(R.id.reviewSubmitButton)

        likeButton = view.findViewById(R.id.likeButton)
        likeCountTextView = view.findViewById(R.id.likeCount)

        mediaListSpinner = view.findViewById(R.id.mediaListSpinner)
        watchlistSpinner = view.findViewById(R.id.watchlistSpinner)

        reviewSubmitButton.setOnClickListener {
            submitReview()
        }

        fetchMediaDetails()
        fetchReviews(currentPage)
        fetchUserListsAndSetupSpinner()

        binding.btnNextReviews.setOnClickListener { goToNextPage() }
        binding.btnPreviousReviews.setOnClickListener { goToPreviousPage() }

        likeButton.setOnClickListener {
            toggleLike()
        }
    }

    /**
     * Fetches media details from the API and updates the UI.
     */
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

    /**
     * Fetches reviews for the current media item from the API.
     * @param page The page number to fetch.
     */
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

    /**
     * Submits a new review for the media item.
     */
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
                    (mediaTitle.text ?: "Unknown title").toString(),
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

    /**
     * Toggles the like status of the media item.
     * Sends a request to the API and updates the UI accordingly.
     */
    private fun toggleLike() {
        val token = AuthManager.getToken(requireContext()) ?: return

        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.likeMedia("Bearer $token", mediaType ?: "movies", mediaId ?: 0)

                if (response.isSuccessful) {
                    isLiked = !isLiked
                    likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                    updateLikeButton()
                    val message = response.body()?.string()
                    ToastUtil.showToast(requireContext(), message ?: "Like status updated")

                } else {
                    val errorMessage = ErrorUtil.parseApiErrorMessage(response)
                    ToastUtil.showToast(requireContext(), errorMessage)
                }
            } catch (e: Exception) {
                ToastUtil.showToast(requireContext(), "Network error: ${e.message}")
            }
        }
    }

    /**
     * Updates the UI elements with the provided media details.
     * @param media The media details retrieved from the API.
     */
    private fun updateUI(media: MediaDetailed) {
        mediaTitle.text = media.title ?: media.name ?: "No title"
        mediaOverview.text = media.overview ?: "No Overview"
        binding.mediaRatingBar.rating = (media.averageRating ?: 0.0).toFloat()

        mediaGenres.text = media.genres.joinToString(", ") { it.name } ?: "No genres"
        mediaExtraInfo.text = when (mediaType) {
            "movies" -> media.runtime?.let { "${it} min" } ?: ""
            "shows" -> media.numberOfEpisodes?.let { "$it episodes" } ?: ""
            else -> ""
        }

        mediaPosterImg = media.backdropPath.toString()

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${media.posterPath}")
            .into(mediaPoster)

        likeCount = media.likeCount ?: 0
        isLiked = media.userHasLiked ?: false

        updateLikeButton()
    }

    /**
     * Moves to the next page of reviews if possible.
     */
    private fun goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchReviews(currentPage)
        }
    }

    /**
     * Moves to the previous page of reviews if possible.
     */
    private fun goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchReviews(currentPage)
        }
    }

    /**
     * Updates the visibility and enabled state of the pagination buttons for the reviews.
     */
    private fun updateButtonVisibility() {
        binding.btnPreviousReviews.visibility = if (currentPage > 1) View.VISIBLE else View.GONE
        binding.btnPreviousReviews.isEnabled = currentPage > 1

        binding.btnNextReviews.visibility = if (currentPage < totalPages) View.VISIBLE else View.GONE
        binding.btnNextReviews.isEnabled = currentPage < totalPages
    }

    /**
     * Updates the like button UI to reflect the current like status.
     */
    private fun updateLikeButton() {
        if (isLiked) {
            likeButton.setImageResource(R.drawable.thumbs_up_filled)
        } else {
            likeButton.setImageResource(R.drawable.thumbs_up_outline)
        }
        likeCountTextView.text = String.format(Locale.getDefault(), "%d", likeCount)
    }

    private fun fetchUserListsAndSetupSpinner() {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getUserProfile("Bearer $token")
                if (response.isSuccessful) {
                    val user = response.body()
                    userMediaLists = user?.lists ?: listOf()
                    userWatchlists = user?.watchlists ?: listOf()

                    val listNames = mutableListOf("Add to media list")
                    listNames.addAll(userMediaLists.map { it.title })

                    val watchlistNames = mutableListOf("Add to watchlist")
                    watchlistNames.addAll(userWatchlists.map { it.title })

                    val mAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listNames)
                    mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    mediaListSpinner.adapter = mAdapter

                    val wAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, watchlistNames)
                    wAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    watchlistSpinner.adapter = wAdapter

                    mediaListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position == 0) return

                            (parent?.getChildAt(0) as TextView).setTextColor(Color.CYAN)

                            val selectedList = userMediaLists[position - 1]
                            lifecycleScope.launch {
                                addToList(selectedList.id, "mediaLists")
                                ToastUtil.showToast(requireContext(), "Added to ${selectedList.title}")
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }

                    watchlistSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position == 0) return

                            val selectedList = userWatchlists[position - 1]
                            lifecycleScope.launch {
                                addToList(selectedList.id, "watchlists")
                                ToastUtil.showToast(requireContext(), "Added to ${selectedList.title}")
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
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

    private fun addToList(id: Int, listType: String) {
        val token = AuthManager.getToken(requireContext()) ?: return

        if (mediaType.toString().isEmpty()) {
            ToastUtil.showToast(requireContext(), "Media type missing!")
            return
        }

        if (mediaId.toString().isEmpty()) {
            ToastUtil.showToast(requireContext(), "Media ID missing!")
            return
        }

        val request = MediaListRequest(
            type = mediaType!!,
            watchlist = false,
            sharedWith = listOf(),
            mediaListItems = listOf(
                MediaListItemRequest(
                    mediaId = mediaId!!,
                    mediaTitle = mediaTitle.text.toString(),
                    mediaSummary = mediaOverview.text.toString(),
                    mediaImg = "https://image.tmdb.org/t/p/w500${mediaPosterImg}",
                    type = mediaType!!
                )
            )
        )

        lifecycleScope.launch {
            try {
                var response: Response<MediaList>? = null

                if (listType == "watchlists") {
                    response = ScreensageApi.retrofitService.updateWatchlist(
                        "Bearer $token",
                        id,
                        replace = false,
                        request
                    )
                }
                else {
                    response = ScreensageApi.retrofitService.updateMediaList(
                        "Bearer $token",
                        id,
                        replace = false,
                        request
                    )
                }

                response?.let {
                    if (!it.isSuccessful) {
                        val errorMessage = ErrorUtil.parseApiErrorMessage(response)
                        println(errorMessage)
                        ToastUtil.showToast(requireContext(), errorMessage)
                    }
                }

            } catch (e: Exception) {
                println("Network error: ${e.message}")
                ToastUtil.showToast(requireContext(), "Network error: ${e.message}")
            }
        }
    }
}
