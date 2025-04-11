package com.example.screensage.ui.auth

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var selectedImageFile: File? = null

    private var cameraImageUri: Uri? = null

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
            showImagePickerDialog()
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

    /**
     * Launcher for taking a photo using the camera.
     * The result is handled by checking if the picture was successfully taken.
     */
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraImageUri != null) {
            selectedImageFile = uriToFile(cameraImageUri!!)
            binding.profileImageView.visibility = View.VISIBLE
            Glide.with(this)
                .load(cameraImageUri)
                .circleCrop()
                .into(binding.profileImageView)
        }
    }

    /**
     * Displays a dialog allowing the user to choose between selecting an image from the gallery or taking a photo.
     * The title is customized with a bold, black TextView.
     */
    private fun showImagePickerDialog() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        val titleTextView = TextView(requireContext()).apply {
            text = "Choose an Action"
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            setPadding(20, 20, 20, 20)
        }

        builder.setCustomTitle(titleTextView)

        val options = arrayOf("Choose from Gallery", "Take Photo")
        builder
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> launchCamera()
                }
            }
            .show()
    }

    /**
     * Opens the system gallery for the user to pick an image.
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    /**
     * Launches the camera app to take a photo.
     * The captured image URI is passed to the camera launcher.
     */
    private fun launchCamera() {
        val imageFile = createImageFile() ?: return
        cameraImageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
        cameraLauncher.launch(cameraImageUri)
    }

    /**
     * Creates a temporary image file to store the photo taken by the camera.
     *
     * @return A new temporary [File], or null if file creation fails.
     */
    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "JPEG_${timeStamp}_"
            val storageDir = requireContext().cacheDir
            File.createTempFile(fileName, ".jpg", storageDir)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
