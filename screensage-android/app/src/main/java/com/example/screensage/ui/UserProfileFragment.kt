package com.example.screensage.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var selectedImageFile: File? = null

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

        binding.profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        loadUserProfile()
        fetchUserReviews(currentPage)
    }

    /**
     * Loads the user profile from the API and updates the UI accordingly.
     */
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

    /**
     * Updates the UI elements with the current user profile data.
     * Sets the username, password placeholder, and profile image in the UI.
     */
    private fun updateUI() {
        usernameText.text = currentUsername
        passwordText.text = currentPasswordPlaceholder
        Glide.with(this)
            .load(currentProfileImage)
            .circleCrop()
            .into(profileImage)
    }

    /**
     * Toggles the edit mode for the user profile.
     * When in edit mode, displays input fields for the username and password.
     * When not in edit mode, hides the inputs and shows the read-only text fields.
     *
     * @param isEditing Boolean value indicating whether edit mode should be enabled or disabled.
     */
    private fun toggleEditMode(isEditing: Boolean) {
        usernameInput.visibility = if (isEditing) View.VISIBLE else View.GONE
        passwordInput.visibility = if (isEditing) View.VISIBLE else View.GONE
        saveProfileButton.visibility = if (isEditing) View.VISIBLE else View.GONE
        usernameText.visibility = if (isEditing) View.GONE else View.VISIBLE
        passwordText.visibility = if (isEditing) View.GONE else View.VISIBLE

        if (isEditing) {
            usernameInput.setText(currentUsername)
        }
    }

    /**
     * Saves the profile changes by updating the username and/or password if they have changed,
     * then updates the UI accordingly.
     * If the username is updated successfully, it also updates the authentication token.
     */
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
                println("Network error: " + e.message)
                ToastUtil.showToast(requireContext(), "Network error: ${e.message}")
            }
        }
    }

    /**
     * Updates the user's profile image, then updates the UI accordingly..
     *
     * @param imageFile The image file to be uploaded. If null, no image will be uploaded.
     */
    private fun updateProfileImage(imageFile: File? = null) {
        val token = AuthManager.getToken(requireContext()) ?: return
        val imagePart = createMultipartBodyImagePart(imageFile)

        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.updateProfileImage(
                    "Bearer $token",
                    imagePart
                )

                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        currentProfileImage = it.profileImg
                    }
                    ToastUtil.showToast(requireContext(), "Profile image updated successfully!")
                } else {
                    val errorMessage = ErrorUtil.parseApiErrorMessage(response)
                    ToastUtil.showToast(requireContext(), errorMessage)
                }
            } catch (e: Exception) {
                println("Network error: " + e.message)
                ToastUtil.showToast(requireContext(), "Network error: ${e.message}")
            }
        }
    }

    /**
     * Converts a file into a [MultipartBody.Part] for image uploads.
     * @param file The image file to be uploaded.
     * @return A [MultipartBody.Part] containing the image file or null if the file is not provided.
     */
    private fun createMultipartBodyImagePart(file: File?): MultipartBody.Part? {
        return file?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }
    }

    /**
     * Converts a [Uri] to a [File] for temporary storage.
     * @param uri The URI of the selected image.
     * @return A [File] containing the image data or null in case of failure.
     */
    private fun uriToFile(uri: Uri): File? {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val file = File(requireContext().cacheDir, "selected_image.jpg")

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Registers an activity result launcher to pick an image from the gallery.
     * If the result is successful, the image is stored as a file and displayed.
     */
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageFile = uriToFile(uri)
                updateProfileImage(selectedImageFile)
                binding.profileImage.setImageURI(uri)
            }
        }
    }

    /**
     * Fetches reviews for the current user from the API.
     * @param page The page number to fetch.
     */
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

    /**
     * Moves to the next page of user reviews if possible.
     */
    private fun goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            fetchUserReviews(currentPage)
        }
    }

    /**
     * Moves to the previous page of user reviews if possible.
     */
    private fun goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            fetchUserReviews(currentPage)
        }
    }

    /**
     * Updates the visibility and enabled state of the pagination buttons for the user reviews.
     */
    private fun updateButtonVisibility() {
        binding.btnPreviousUserReviews.visibility = if (currentPage > 1) View.VISIBLE else View.GONE
        binding.btnPreviousUserReviews.isEnabled = currentPage > 1

        binding.btnNextUserReviews.visibility = if (currentPage < totalPages) View.VISIBLE else View.GONE
        binding.btnNextUserReviews.isEnabled = currentPage < totalPages
    }

}
