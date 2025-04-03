package com.example.screensage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.screensage.R
import com.example.screensage.databinding.FragmentMediaListsBinding
import com.example.screensage.entities.MediaList
import com.example.screensage.network.MediaListResponse
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.MediaListsAdapter
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.launch
import retrofit2.Response

class MediaListsFragment : Fragment() {
    private var _binding: FragmentMediaListsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var mediaListAdapter: MediaListsAdapter
    private var mediaLists: List<MediaList> = listOf()

    private var currentPage = 1
    private var totalPages = 1

    private var listType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listType = it.getString("listType")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_media, container, false)
        _binding = FragmentMediaListsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.listRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mediaListAdapter = MediaListsAdapter(mediaLists) { mediaListId ->
            val bundle = Bundle().apply {
                putString("listType", listType)
                putInt("listId", mediaListId)
            }
            findNavController().navigate(R.id.nav_media_list, bundle)
        }
        recyclerView.adapter = mediaListAdapter

        fetchMediaLists(currentPage)

        binding.btnNext.setOnClickListener { goToNextPage() }
        binding.btnPrevious.setOnClickListener { goToPreviousPage() }
    }

    /**
     * Fetches a paginated list of media lists.
     * Updates the RecyclerView with the retrieved list of media lists.
     * @param page The page number to fetch.
     */
    private fun fetchMediaLists(page: Int) {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                var response: Response<MediaListResponse>? = null

                if (listType == "mediaLists") {
                    response = ScreensageApi.retrofitService.getMediaLists(
                        "Bearer ${token}",
                        page
                    )
                }
                else if (listType == "myMediaLists") {
                    response = ScreensageApi.retrofitService.getMyMediaLists(
                        "Bearer ${token}",
                        page
                    )
                }
                else if (listType == "watchlists") {
                    response = ScreensageApi.retrofitService.getWatchlists(
                        "Bearer ${token}",
                        page
                    )
                }

                response?.let {
                    if (it.isSuccessful) {
                        val mediaListsResponse = it.body()
                        if (mediaListsResponse != null) {
                            mediaLists = mediaListsResponse.content
                            mediaListAdapter.updateMedia(mediaLists)
                            totalPages = mediaListsResponse.totalPages
                            updateButtonVisibility()
                        }

                    } else {
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

    /**
     * Navigates to the next page of media list items if available.
     */
    private fun goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchMediaLists(currentPage)
        }
    }

    /**
     * Navigates to the previous page of media list items if available.
     */
    private fun goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchMediaLists(currentPage)
        }
    }

    /**
     * Updates the visibility and state of pagination buttons based on the current page.
     */
    private fun updateButtonVisibility() {
        binding.btnPrevious.visibility = if (currentPage > 1) View.VISIBLE else View.GONE
        binding.btnPrevious.isEnabled = currentPage > 1

        binding.btnNext.visibility = if (currentPage < totalPages) View.VISIBLE else View.GONE
        binding.btnNext.isEnabled = currentPage < totalPages
    }
}
