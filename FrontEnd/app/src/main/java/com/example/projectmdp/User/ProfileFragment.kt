package com.example.projectmdp.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.projectmdp.R
import com.example.projectmdp.databinding.FragmentProfileBinding
import com.example.projectmdp.utils.SessionManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil nama dari SessionManager dan tampilkan
        val userName = SessionManager.getUserName(requireContext())
        if (!userName.isNullOrEmpty()) {
            binding.tvWelcomeMessage.text = "Profil: $userName"
        } else {
            binding.tvWelcomeMessage.text = "Profil Pengguna"
        }

        // Setup listener untuk tombol logout
        binding.btnLogout.setOnClickListener {
            // Hapus sesi/token
            SessionManager.clearAuthToken(requireContext())

            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(R.id.action_homeUserFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}