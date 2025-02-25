package com.example.screensage.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.screensage.R
import com.example.screensage.Util.ErrorUtil
import com.example.screensage.Util.ToastUtil
import com.example.screensage.service.AuthManager
import com.example.screensage.databinding.FragmentLoginBinding
import com.example.screensage.network.LoginRequest
import com.example.screensage.network.ScreensageApi
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                ToastUtil.showToast(requireContext(), "Please fill in all fields")
                return@setOnClickListener
            }

            loginUser(username, password)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun loginUser(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = ScreensageApi.retrofitService.login(LoginRequest(username, password))

                if (response.isSuccessful) {
                    val jwtPayload = response.body()
                    if (jwtPayload != null) {
                        AuthManager.saveToken(requireContext(), jwtPayload.token)
                        ToastUtil.showToast(requireContext(), "Logged in successfully.")
                        // findNavController().navigate(R.id.nav_home)
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        println("Unexpected response from server")
                        ToastUtil.showToast(requireContext(), "Unexpected response from server")
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
