package com.example.screensage.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.screensage.R
import com.example.screensage.utils.MediaAdapter
import com.example.screensage.entities.Media
import com.example.screensage.databinding.FragmentMediaBinding
import com.example.screensage.entities.Genre
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.launch

class MediaFragment : Fragment() {
    private var _binding: FragmentMediaBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var mediaAdapter: MediaAdapter
    private var mediaList: List<Media> = listOf()

    private var currentPage = 1
    private var totalPages = 1
    private var selectedGenreId: Int? = null
    private var searchQuery: String? = null
    private var genreList: List<Genre> = listOf()

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private var mediaType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mediaType = it.getString("mediaType")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_media, container, false)
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mediaAdapter = MediaAdapter(mediaList) { mediaId ->
            val bundle = Bundle().apply {
                putInt("mediaId", mediaId)
                putString("mediaType", mediaType)
            }
            findNavController().navigate(R.id.mediaDetailedFragment, bundle)
        }
        recyclerView.adapter = mediaAdapter

        fetchGenres()
        fetchMedia(currentPage)

        setupSearchView()
        setupGenreDropdown()

        binding.btnNext.setOnClickListener { goToNextPage() }
        binding.btnPrevious.setOnClickListener { goToPreviousPage() }
    }

    /**
     * Fetches the list of available genres for the selected media type.
     * Updates the genre dropdown menu with the retrieved genres.
     */
    private fun fetchGenres() {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getMediaGenres(
                    "Bearer ${token}",
                    mediaType ?: "movies"
                )
                if (response.isSuccessful) {
                    val genreResponse = response.body()
                    genreResponse?.let {
                        genreList = it.genres

                        val genreNames = mutableListOf("All Genres") + genreList.map { it.name }
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genreNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerGenre.adapter = adapter
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
     * Fetches a paginated list of media items based on the selected genre and search query.
     * Updates the RecyclerView with the retrieved media list.
     * @param page The page number to fetch.
     */
    private fun fetchMedia(page: Int) {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getMedia(
                    "Bearer ${token}",
                    mediaType ?: "movies",
                    page,
                    selectedGenreId,
                    searchQuery
                )
                if (response.isSuccessful) {
                    val mediaResponse = response.body()
                    if (mediaResponse != null) {
                        mediaList = mediaResponse.results
                        mediaAdapter.updateMedia(mediaList)
                        totalPages = mediaResponse.totalPages
                        updateButtonVisibility()
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
     * Sets up the search view to filter media items based on user input.
     * Implements debounce to optimize network requests.
     */
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchRunnable?.let { handler.removeCallbacks(it) }
                searchQuery = query

                selectedGenreId = null
                binding.spinnerGenre.setSelection(0)

                fetchMedia(1)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { handler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    searchQuery = newText
                    fetchMedia(1)
                }
                handler.postDelayed(searchRunnable!!, 500)
                return true
            }
        })
    }

    /**
     * Sets up the genre dropdown menu.
     * Updates the media items when a genre is selected.
     */
    private fun setupGenreDropdown() {
        binding.spinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGenreId = if (position == 0) null else genreList[position - 1].id

                searchQuery = null
                binding.searchView.setQuery("", false)
                binding.searchView.clearFocus()
                binding.searchView.isIconified = true

                fetchMedia(1)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    /**
     * Navigates to the next page of media items if available.
     */
    private fun goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchMedia(currentPage)
        }
    }

    /**
     * Navigates to the previous page of media items if available.
     */
    private fun goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchMedia(currentPage)
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
