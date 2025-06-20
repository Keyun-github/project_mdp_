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
import com.example.projectmdp.api.RegisterRequest
import com.example.projectmdp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup listener untuk tombol register
        binding.btnRegister.setOnClickListener {
            handleRegistration()
        }

        // Setup listener untuk teks "Login"
        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        // Mulai mengamati perubahan dari ViewModel
        observeViewModel()
    }

    /**
     * Fungsi untuk menangani logika saat tombol register ditekan.
     */
    private fun handleRegistration() {
        val namaLengkap = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordConfirmation.text.toString()

        // Validasi input di sisi frontend
        if (namaLengkap.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
            return // Hentikan fungsi jika ada data yang kosong
        }

        if (password.length < 6) {
            Toast.makeText(requireContext(), "Password minimal harus 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Password dan konfirmasi tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        // Jika semua validasi frontend lolos, buat request dan kirim ke ViewModel
        val request = RegisterRequest(namaLengkap, email, password, confirmPassword)
        authViewModel.registerUser(request)
    }

    /**
     * Fungsi untuk mengamati LiveData dari AuthViewModel.
     * Ini akan bereaksi terhadap perubahan state (Loading, Success, Error).
     */
    private fun observeViewModel() {
        authViewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Loading -> {
                    // Saat sedang loading, nonaktifkan tombol dan ubah teksnya
                    binding.btnRegister.isEnabled = false
                    binding.btnRegister.text = "Loading..."
                }
                is ApiResult.Success -> {
                    // Jika berhasil, aktifkan kembali tombol
                    binding.btnRegister.isEnabled = true
                    binding.btnRegister.text = "Register"

                    // Tampilkan pesan sukses dari server
                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_LONG).show()

                    // Navigasi ke halaman login setelah registrasi berhasil
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is ApiResult.Error -> {
                    // Jika gagal, aktifkan kembali tombol
                    binding.btnRegister.isEnabled = true
                    binding.btnRegister.text = "Register"

                    // Coba parse pesan error dari JSON agar lebih mudah dibaca
                    val errorMessage = try {
                        org.json.JSONObject(result.message).getString("message")
                    } catch (e: Exception) {
                        result.message // Jika gagal parse, tampilkan pesan mentah
                    }
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}