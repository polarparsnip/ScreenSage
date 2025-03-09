package com.example.screensage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screensage.R
import com.example.screensage.databinding.FragmentUserProfileBinding
import com.example.screensage.network.PasswordRequest
import com.example.screensage.network.ScreensageApi
import com.example.screensage.network.UsernameRequest
import com.example.screensage.service.AuthManager
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.ReviewAdapter
import com.example.screensage.utils.ToastUtil
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var profileImage: ImageView
    private lateinit var usernameText: TextView
    private lateinit var passwordText: TextView
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var saveProfileButton: Button
    private lateinit var editProfileButton: Button

    private var currentUsername: String = ""
    private var currentPasswordPlaceholder: String = ""
    private var currentProfileImage: String = ""

    private var editMode = false

    private lateinit var userReviewsRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private var totalPages = 1
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // val root = inflater.inflate(R.layout.fragment_user_profile, container, false)
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImage = view.findViewById(R.id.profileImage)
        usernameText = view.findViewById(R.id.usernameText)
        passwordText = view.findViewById(R.id.passwordText)
        usernameInput = view.findViewById(R.id.usernameInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        saveProfileButton = view.findViewById(R.id.saveProfileButton)
        editProfileButton = view.findViewById(R.id.editProfileButton)

        userReviewsRecyclerView = view.findViewById(R.id.userReviewsRecyclerView)
        userReviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        reviewAdapter = ReviewAdapter(mutableListOf())
        userReviewsRecyclerView.adapter = reviewAdapter

        editProfileButton.setOnClickListener {
            editMode = !editMode
            toggleEditMode(editMode)
            editProfileButton.text = if (editMode) "Cancel" else "Edit profile"
        }

        saveProfileButton.setOnClickListener {
            saveProfileChanges()
        }

        binding.btnNextUserReviews.setOnClickListener { goToNextPage() }
        binding.btnPreviousUserReviews.setOnClickListener { goToPreviousPage() }

        loadUserProfile()
        fetchUserReviews(currentPage)
    }

    private fun loadUserProfile() {
        val token = AuthManager.getToken(requireContext()) ?: return

        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getUserProfile("Bearer $token")
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        currentUsername = it.username
                        currentPasswordPlaceholder = it.passwordPlaceholder
                        currentProfileImage = it.profileImg
                        updateUI()
                    }
                } else {
                    val errorMessage = ErrorUtil.parseApiErrorMessage(response)
                    println(errorMessage)
                    ToastUtil.showToast(requireContext(), errorMessage)
                }
            } catch (e: Exception) {
                ToastUtil.showToast(requireContext(), "Network error: ${e.message}")
            }
        }
    }

    private fun updateUI() {
        usernameText.text = currentUsername
        passwordText.text = currentPasswordPlaceholder
        Glide.with(this)
            .load(currentProfileImage)
            .circleCrop()
            .into(profileImage)
    }

    private fun toggleEditMode(isEditing: Boolean) {
        usernameInput.visibility = if (isEditing) View.VISIBLE else View.GONE
        passwordInput.visibility = if (isEditing) View.VISIBLE else View.GONE
        saveProfileButton.visibility = if (isEditing) View.VISIBLE else View.GONE
        usernameText.visibility = if (isEditing) View.GONE else View.VISIBLE
        passwordText.visibility = if (isEditing) View.GONE else View.VISIBLE

        if (isEditing) {
            usernameInput.setText(currentUsername)
            // passwordInput.setText("****") // Placeholder for the password
        }
    }

    private fun saveProfileChanges() {
        val newUsername = usernameInput.text.toString()
        val newPassword = passwordInput.text.toString()

        if (
            (newUsername.isEmpty() || usernameInput.text.toString() == usernameText.text.toString())
            && newPassword.isEmpty()
        ) {
            ToastUtil.showToast(requireContext(), "Username and password are unchanged")
            return
        }

        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                if (newUsername.isNotEmpty() && usernameInput.text.toString() != usernameText.text.toString()) {
                    val responseUsername = ScreensageApi.retrofitService.updateUsername(
                        "Bearer $token",
                        UsernameRequest(newUsername)
                    )

                    if (responseUsername.isSuccessful) {
                        val updatedToken = responseUsername.body()?.token ?: return@launch
                        AuthManager.saveToken(requireContext(), updatedToken)

                        currentUsername = newUsername
                        usernameInput.text.clear()
                    } else {
                        val errorMessage = ErrorUtil.parseApiErrorMessage(responseUsername)
                        println(errorMessage)
                        ToastUtil.showToast(requireContext(), errorMessage)
                    }
                }

                if (newPassword.isNotEmpty()) {
                    val responsePassword = ScreensageApi.retrofitService.updatePassword(
                        "Bearer $token",
                        PasswordRequest(newPassword)
                    )

                    if (responsePassword.isSuccessful) {
                        currentPasswordPlaceholder = responsePassword.body()?.passwordPlaceholder!!
                        passwordInput.text.clear()
                    } else {
                        val errorMessage = ErrorUtil.parseApiErrorMessage(responsePassword)
                        println(errorMessage)
                        ToastUtil.showToast(requireContext(), errorMessage)
                    }
                }

                toggleEditMode(false)
                editProfileButton.text = "Edit profile"
                updateUI()
                ToastUtil.showToast(requireContext(), "Profile updated successfully!")

            } catch (e: Exception) {
                ToastUtil.showToast(requireContext(), "Network error: ${e.message}")
            }
        }
    }

    private fun fetchUserReviews(page: Int) {
        val token = AuthManager.getToken(requireContext()) ?: return
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.getUserReviews(
                    "Bearer $token",
                    page
                )
                if (response.isSuccessful) {
                    response.body()?.let { reviewResponse ->
                        println("concon: " + reviewResponse.content)
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

    private fun goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchUserReviews(currentPage)
        }
    }

    private fun goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchUserReviews(currentPage)
        }
    }

    private fun updateButtonVisibility() {
        binding.btnPreviousUserReviews.visibility = if (currentPage > 1) View.VISIBLE else View.GONE
        binding.btnPreviousUserReviews.isEnabled = currentPage > 1

        binding.btnNextUserReviews.visibility = if (currentPage < totalPages) View.VISIBLE else View.GONE
        binding.btnNextUserReviews.isEnabled = currentPage < totalPages
    }

}
