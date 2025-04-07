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
import com.example.screensage.databinding.FragmentMediaListBinding
import com.example.screensage.entities.MediaListItem
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.MediaListAdapter
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.launch

class MediaListFragment : Fragment() {
    private var _binding: FragmentMediaListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var mediaListAdapter: MediaListAdapter
    private var mediaList: List<MediaListItem> = listOf()

    private var listType: String? = null
    private var listId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listType = it.getString("listType")
            listId = it.getInt("listId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_media, container, false)
        _binding = FragmentMediaListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mediaListAdapter = MediaListAdapter(mediaList) { mediaId, mediaType ->
            val bundle = Bundle().apply {
                putInt("mediaId", mediaId)
                putString("mediaType", mediaType)
            }
            findNavController().navigate(R.id.mediaDetailedFragment, bundle)
        }
        recyclerView.adapter = mediaListAdapter

        if (listType == "watchlists") {
            fetchWatchlist(listId!!)
        } else {
            fetchMediaList(listId!!)
        }
    }

    /**
     * Fetches a list of media list items based on the selected ID.
     * Updates the RecyclerView with the retrieved media list.
     */
    private fun fetchMediaList(id: Int) {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getMediaList(
                    "Bearer ${token}",
                    id
                )
                if (response.isSuccessful) {
                    val mediaResponse = response.body()
                    if (mediaResponse != null) {
                        mediaList = mediaResponse.mediaListItems
                        mediaListAdapter.updateMedia(mediaList)
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
     * Fetches a list of watchlist items based on the selected ID.
     * Updates the RecyclerView with the retrieved media list.
     */
    private fun fetchWatchlist(id: Int) {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getWatchlist(
                    "Bearer ${token}",
                    id
                )
                if (response.isSuccessful) {
                    val mediaResponse = response.body()
                    if (mediaResponse != null) {
                        mediaList = mediaResponse.mediaListItems
                        mediaListAdapter.updateMedia(mediaList)
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

}
