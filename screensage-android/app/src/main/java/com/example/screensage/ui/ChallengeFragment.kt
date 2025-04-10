package com.example.screensage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.screensage.R
import com.example.screensage.databinding.FragmentChallengeBinding
import com.example.screensage.entities.Challenge
import com.example.screensage.network.ScreensageApi
import com.example.screensage.service.AuthManager
import com.example.screensage.service.RoomService
import com.example.screensage.service.User
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChallengeFragment : Fragment() {
    private var _binding: FragmentChallengeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var challengeImage: ImageView
    private lateinit var questionText: TextView
    private lateinit var optionButtons: List<Button>

    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // val root = inflater.inflate(R.layout.fragment_user_profile, container, false)
        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        challengeImage = view.findViewById(R.id.challengeImage)

        questionText = view.findViewById(R.id.challengeQuestion)

        CoroutineScope(Dispatchers.Main).launch {
            val user = RoomService.getCurrentUser(requireContext())
            if (user != null) {
                currentUser = user
            }

            optionButtons = listOf(
                view.findViewById(R.id.option1),
                view.findViewById(R.id.option2),
                view.findViewById(R.id.option3),
                view.findViewById(R.id.option4)
            )

            loadChallenge()
        }
    }

    /**
     * Loads the challenge from the API and updates the UI accordingly.
     */
    private fun loadChallenge() {
        val token = AuthManager.getToken(requireContext()) ?: return

        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getChallenge("Bearer $token")
                if (response.isSuccessful) {
                    val challenge = response.body()
                    challenge?.let {
                        updateUI(challenge)
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
     * Submits an answer for a challenge.
     */
    private fun submitChallengeAnswer(challenge: Challenge, optionId: Int, userId: Int) {
        val token = AuthManager.getToken(requireContext()) ?: return

        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.postChallengeAnswer(
                    "Bearer $token",
                    challenge.id,
                    optionId,
                    userId
                )

                if (response.isSuccessful) {
                    val result = response.body()
                    result?.let {
                        if (result.answeredCorrectly) {
                            challenge.options.forEachIndexed { index, option ->
                                val button = optionButtons[index]
                                button.isEnabled = false
                                if (option.id == optionId) {
                                    button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_500))
                                } else {
                                    button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_gray_500))
                                }
                            }
                            ToastUtil.showToast(requireContext(), "You answered correctly!")
                        } else {
                            challenge.options.forEachIndexed { index, option ->
                                val button = optionButtons[index]
                                button.isEnabled = false
                                if (option.id == optionId) {
                                    button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_500))
                                } else if (option.id == result.correctOption.id) {
                                    button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow_500))
                                } else {
                                    button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_gray_500))
                                }
                            }
                            ToastUtil.showToast(requireContext(), "Incorrect! answer was: ${result.correctOption.option}")
                        }
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
     * Updates the UI elements with the challenge data.
     * Sets the question, image, and challenge options in the UI.
     */
    private fun updateUI(challenge: Challenge) {
        questionText.text = challenge.question
        Glide.with(this)
            .load(challenge.image)
            .into(challengeImage)

        challenge.options.forEachIndexed { index, option ->
            val button = optionButtons[index]
            button.text = option.option
            button.setOnClickListener {
                submitChallengeAnswer(challenge, option.id, currentUser?.id ?: 0)
            }
        }
    }

}
