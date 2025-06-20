package com.example.projectmdp.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.projectmdp.R
import com.example.projectmdp.api.LoginRequest
import com.example.projectmdp.databinding.FragmentLoginBinding
import com.example.projectmdp.utils.SessionManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi ViewModel menggunakan Factory agar bisa menerima Context
    // untuk membuat instance AuthRepository yang terhubung ke Room.
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Cek jika user sudah login sebelumnya
        val token = SessionManager.getAuthToken(requireContext())
        if (!token.isNullOrEmpty()) {
            findNavController().navigate(R.id.action_loginFragment_to_homeUserFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        observeViewModel()
    }

    private fun handleLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(email, password)
        authViewModel.loginUser(loginRequest)
    }

    /**
     * Fungsi untuk mengamati LiveData dari AuthViewModel.
     * Ini akan bereaksi terhadap perubahan state (Loading, Success, Error).
     * Bagian inilah yang diperbaiki untuk mengatasi error 'when' expression must be exhaustive.
     */
    private fun observeViewModel() {
        // Pastikan untuk mengamati loginResult, bukan registrationResult
        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            // Gunakan nama sealed class yang benar: ApiResult
            when (result) {
                is ApiResult.Loading -> {
                    binding.btnLogin.isEnabled = false
                    binding.btnLogin.text = "Loading..."
                }
                is ApiResult.Success -> {
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Login"

                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()

                    val responseData = result.data
                    SessionManager.saveAuthToken(requireContext(), responseData.token)
                    SessionManager.saveUserName(requireContext(), responseData.user.namaLengkap)

                    findNavController().navigate(R.id.action_loginFragment_to_homeUserFragment)
                }
                is ApiResult.Error -> {
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Login"

                    val errorMessage = try {
                        org.json.JSONObject(result.message).getString("message")
                    } catch (e: Exception) {
                        result.message
                    }
                    Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}