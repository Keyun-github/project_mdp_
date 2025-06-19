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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            val request = LoginRequest(email, password)
            authViewModel.loginUser(request)
        }

        // Logika untuk ke Register
        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    binding.btnLogin.isEnabled = false
                    binding.btnLogin.text = "Loading..."
                }
                is AuthResult.Success -> {
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Login"
                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()

                    // TODO: Simpan token (misal: di SharedPreferences)
                    // contoh: saveToken(result.data.token)

                    // TODO: Navigasi ke halaman utama aplikasi setelah login
                    // findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                is AuthResult.Error -> {
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Login"
                    Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}