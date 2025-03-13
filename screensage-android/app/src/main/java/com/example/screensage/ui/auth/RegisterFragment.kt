package com.example.screensage.ui.auth

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.screensage.R
import com.example.screensage.utils.ErrorUtil
import com.example.screensage.utils.ToastUtil
import com.example.screensage.databinding.FragmentRegisterBinding
import com.example.screensage.network.ScreensageApi
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var selectedImageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (password == confirmPassword) {
                registerUser(username, password, selectedImageFile)
            } else {
                ToastUtil.showToast(requireContext(), "Passwords do not match")
            }
        }

        binding.backToLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Creates a [RequestBody] for text data.
     * @param value The string value to convert into a request body.
     * @return The created [RequestBody].
     */
    private fun createTextRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
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
                binding.profileImageView.setVisibility(View.VISIBLE)
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.profileImageView)
                // binding.profileImageView.setImageURI(uri)
            }
        }
    }

    /**
     * Sends a registration request to the API with the provided user details.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @param imageFile The optional profile image file.
     */
    private fun registerUser(username: String, password: String, imageFile: File? = null) {
        val usernamePart = createTextRequestBody(username)
        val passwordPart = createTextRequestBody(password)
        val imagePart = createMultipartBodyImagePart(imageFile)

        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.register(usernamePart, passwordPart, imagePart)

                if (response.isSuccessful) {
                    ToastUtil.showToast(requireContext(), "Registration successful! Please log in.")
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
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
