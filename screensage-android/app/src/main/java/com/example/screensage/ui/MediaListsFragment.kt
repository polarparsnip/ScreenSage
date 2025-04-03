package com.example.screensage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.screensage.R
import com.example.screensage.databinding.FragmentMediaListsBinding
import com.example.screensage.entities.MediaList
import com.example.screensage.network.MediaListPostRequest
import com.example.screensage.network.MediaListResponse
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.service.RoomService
import com.example.screensage.service.User
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.MediaListsAdapter
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
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

    private lateinit var listTitleInput: EditText
    private lateinit var listDescriptionInput: EditText
    private lateinit var listSubmitButton: Button

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

        listTitleInput = view.findViewById(R.id.listTitleInput)
        listDescriptionInput = view.findViewById(R.id.listDescriptionInput)
        listSubmitButton = view.findViewById(R.id.listSubmitButton)

        recyclerView = view.findViewById(R.id.listRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.Main).launch {
            var currentUser: User? = null
            val user = RoomService.getCurrentUser(requireContext())
            if (user != null) {
                currentUser = user
            }

            mediaListAdapter = MediaListsAdapter(
                mediaLists,
                currentUser,
                onItemClick = { mediaListId ->
                    val bundle = Bundle().apply {
                        putString("listType", listType)
                        putInt("listId", mediaListId)
                    }
                    findNavController().navigate(R.id.nav_media_list, bundle)
                },
                onIconClick = { mediaListId ->
                    deleteList(mediaListId)
                },
            )
            recyclerView.adapter = mediaListAdapter

            fetchMediaLists(currentPage)

            listSubmitButton.setOnClickListener {
                createList()
            }

            binding.btnNext.setOnClickListener { goToNextPage() }
            binding.btnPrevious.setOnClickListener { goToPreviousPage() }
        }
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
     * Creates a new media list / watchlist.
     */
    private fun createList() {
        val token = AuthManager.getToken(requireContext()) ?: return

        val title = listTitleInput.text.toString().trim()
        val description = listDescriptionInput.text.toString().trim()

        if (title.isEmpty()) {
            ToastUtil.showToast(requireContext(), "List title cannot be empty!")
            return
        }
        if (description.isEmpty()) {
            ToastUtil.showToast(requireContext(), "List description cannot be empty!")
            return
        }

        lifecycleScope.launch {
            try {
                var response: Response<MediaList>? = null

                if (listType == "watchlists") {
                    val listRequest = MediaListPostRequest(title, description, true)
                    response = ScreensageApi.retrofitService.createWatchlist(
                        "Bearer ${token}",
                        listRequest
                    )
                }
                else {
                    val listRequest = MediaListPostRequest(title, description, false)
                    response = ScreensageApi.retrofitService.createMediaList(
                        "Bearer ${token}",
                        listRequest
                    )
                }

                if (response.isSuccessful) {
                    val newList = response.body()
                    newList?.let {
                        mediaLists = mediaLists + it
                        mediaListAdapter.updateMedia(mediaLists)
                        listTitleInput.text.clear()
                        listDescriptionInput.text.clear()

                        ToastUtil.showToast(requireContext(), "List has been created!")
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
     * Deletes a media list / watchlist.
     */
    private fun deleteList(id: Int) {
        val token = AuthManager.getToken(requireContext()) ?: return

        lifecycleScope.launch {
            try {
                var response: Response<ResponseBody>? = null

                if (listType == "watchlists") {
                    response = ScreensageApi.retrofitService.deleteWatchlist(
                        "Bearer ${token}",
                        id
                    )
                }
                else {
                    response = ScreensageApi.retrofitService.deleteMediaList(
                        "Bearer ${token}",
                        id
                    )
                }

                if (response.isSuccessful) {
                    val res = response.body()
                    res?.let {
                        var newMediaLists = mediaLists.filterNot { it.id == id }
                        newMediaLists = newMediaLists.sortedByDescending { it.createdAt }
                        mediaListAdapter.updateMedia(newMediaLists)
                        ToastUtil.showToast(requireContext(), "List has been deleted!")
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
