package com.example.projectmdp.Login

import android.os.Bundle
import android.util.Log
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
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val namaLengkap = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = "dummy_confirm_password" // Backend Anda butuh ini, sesuaikan jika beda

            val request = RegisterRequest(namaLengkap, email, password, confirmPassword)
            authViewModel.registerUser(request)
        }

        // Logika untuk kembali ke Login
        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        authViewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    // Tampilkan progress bar
                    binding.btnRegister.isEnabled = false
                    binding.btnRegister.text = "Loading..."
                }
                is AuthResult.Success -> {
                    binding.btnRegister.isEnabled = true
                    binding.btnRegister.text = "Register"
                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()
                    // Navigasi ke halaman login setelah berhasil
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is AuthResult.Error -> {
                    binding.btnRegister.isEnabled = true
                    binding.btnRegister.text = "Register"
                    Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_LONG).show()
                    Log.e("Registration Error", result.message)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}