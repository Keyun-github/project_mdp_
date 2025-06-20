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
            registerUser()
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        observeViewModel()
    }

    private fun registerUser() {
        val namaLengkap = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordConfirmation.text.toString()

        // Validasi di sisi frontend terlebih dahulu
        if (namaLengkap.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Password dan konfirmasi tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(requireContext(), "Password minimal harus 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        // Jika semua validasi lolos, baru kirim ke ViewModel
        val request = RegisterRequest(namaLengkap, email, password, confirmPassword)
        authViewModel.registerUser(request)
    }

    private fun observeViewModel() {
        authViewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    binding.btnRegister.isEnabled = false
                    binding.btnRegister.text = "Loading..."
                }
                is AuthResult.Success -> {
                    binding.btnRegister.isEnabled = true
                    binding.btnRegister.text = "Register"
                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is AuthResult.Error -> {
                    binding.btnRegister.isEnabled = true
                    binding.btnRegister.text = "Register"
                    val errorMessage = try {
                        org.json.JSONObject(result.message).getString("message")
                    } catch (e: Exception) {
                        result.message
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