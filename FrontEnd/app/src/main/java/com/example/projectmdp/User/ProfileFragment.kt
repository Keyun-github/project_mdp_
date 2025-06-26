package com.example.projectmdp.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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

        displayUserInfo()

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.btnLogout.setOnClickListener {
            SessionManager.clearSession(requireContext())
            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(R.id.action_homeUserFragment_to_loginFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        // Perbarui info user setiap kali fragment ini ditampilkan
        displayUserInfo()
    }

    private fun displayUserInfo() {
        val userName = SessionManager.getUserName(requireContext())
        binding.tvProfileName.text = "Nama: ${userName ?: "Tidak tersedia"}"
        // Anda juga bisa menyimpan dan menampilkan email jika mau
        // binding.tvProfileEmail.text = "Email: ..."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}