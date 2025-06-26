package com.example.projectmdp.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
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

    // Inisialisasi ViewModel menggunakan Factory
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
        // Cek jika pengguna sudah memiliki sesi (token) yang tersimpan.
        // Jika ya, langsung navigasi ke home tanpa perlu login ulang.
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

    private fun observeViewModel() {
        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            // Mengatur visibilitas tombol dan input field berdasarkan state
            val isLoading = result is ApiResult.Loading
            binding.btnLogin.isEnabled = !isLoading
            binding.tilEmail.isEnabled = !isLoading
            binding.tilPassword.isEnabled = !isLoading
            // Anda bisa menambahkan ProgressBar dan menampilkannya di sini jika mau
            // binding.progressBar.isVisible = isLoading

            if (isLoading) {
                binding.btnLogin.text = "Loading..."
            } else {
                binding.btnLogin.text = "Login"
            }

            when (result) {
                is ApiResult.Success -> {
                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()

                    // Simpan semua data sesi yang relevan
                    val responseData = result.data
                    SessionManager.saveAuthToken(requireContext(), responseData.token)
                    SessionManager.saveUserId(requireContext(), responseData.user.id)
                    SessionManager.saveUserName(requireContext(), responseData.user.namaLengkap)
                    SessionManager.saveUserEmail(requireContext(), responseData.user.email)

                    // Navigasi ke halaman utama pengguna
                    findNavController().navigate(R.id.action_loginFragment_to_homeUserFragment)
                }
                is ApiResult.Error -> {
                    // Coba parse pesan error dari JSON agar lebih mudah dibaca
                    val errorMessage = try {
                        org.json.JSONObject(result.message).getString("message")
                    } catch (e: Exception) {
                        result.message
                    }
                    Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_LONG).show()
                }
                // Kasus Loading sudah ditangani di atas
                is ApiResult.Loading -> { }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}