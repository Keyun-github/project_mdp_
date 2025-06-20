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

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val token = SessionManager.getAuthToken(requireContext())
        if (!token.isNullOrEmpty()) {
            findNavController().navigate(R.id.action_loginFragment_to_homeUserFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup listener untuk tombol login
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        // Setup listener untuk teks "Register"
        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        observeViewModel()
    }

    private fun handleLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        // Validasi input sederhana di sisi frontend
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(email, password)
        authViewModel.loginUser(loginRequest)
    }

    private fun observeViewModel() {
        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    // Saat sedang loading, nonaktifkan tombol dan ubah teksnya
                    binding.btnLogin.isEnabled = false
                    binding.btnLogin.text = "Loading..."
                }
                is AuthResult.Success -> {
                    // Jika berhasil, aktifkan kembali tombol
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Login"

                    // Tampilkan pesan sukses dari server
                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()

                    // Simpan data sesi (token dan nama pengguna)
                    val responseData = result.data
                    SessionManager.saveAuthToken(requireContext(), responseData.token)
                    SessionManager.saveUserName(requireContext(), responseData.user.namaLengkap)

                    // Navigasi ke halaman utama pengguna
                    findNavController().navigate(R.id.action_loginFragment_to_homeUserFragment)
                }
                is AuthResult.Error -> {
                    // Jika gagal, aktifkan kembali tombol
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Login"

                    // Coba parse pesan error dari JSON agar lebih mudah dibaca
                    val errorMessage = try {
                        org.json.JSONObject(result.message).getString("message")
                    } catch (e: Exception) {
                        result.message // Jika gagal parse, tampilkan pesan mentah
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