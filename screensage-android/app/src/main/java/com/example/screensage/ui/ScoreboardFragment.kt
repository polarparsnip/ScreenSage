package com.example.screensage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.screensage.R
import com.example.screensage.databinding.FragmentScoreboardBinding
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.ScoreboardAdapter
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.launch

class ScoreboardFragment : Fragment() {
    private var _binding: FragmentScoreboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var scoreboardAdapter: ScoreboardAdapter

    private var totalPages = 1
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScoreboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scoreboardAdapter = ScoreboardAdapter(listOf(), currentPage)

        recyclerView = view.findViewById(R.id.scoreboardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = scoreboardAdapter

        fetchScoreboard(currentPage)

        binding.btnNextReviews.setOnClickListener { goToNextPage() }
        binding.btnPreviousReviews.setOnClickListener { goToPreviousPage() }

    }

    /**
     * Fetches challenge scoreboard from the API and updates the UI.
     */
    private fun fetchScoreboard(page: Int) {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getScoreboard(
                    "Bearer ${token}",
                    page
                )
                if (response.isSuccessful) {
                    response.body()?.let { scoreboard ->
                        totalPages = scoreboard.totalPages
                        scoreboardAdapter.updateScores(scoreboard.content, currentPage)
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
     * Moves to the next page of user scores if possible.
     */
    private fun goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchScoreboard(currentPage)
        }
    }

    /**
     * Moves to the previous page of user scores if possible.
     */
    private fun goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchScoreboard(currentPage)
        }
    }

    /**
     * Updates the visibility and enabled state of the pagination buttons for the scoreboard.
     */
    private fun updateButtonVisibility() {
        binding.btnPreviousReviews.visibility = if (currentPage > 1) View.VISIBLE else View.GONE
        binding.btnPreviousReviews.isEnabled = currentPage > 1

        binding.btnNextReviews.visibility = if (currentPage < totalPages) View.VISIBLE else View.GONE
        binding.btnNextReviews.isEnabled = currentPage < totalPages
    }
}
