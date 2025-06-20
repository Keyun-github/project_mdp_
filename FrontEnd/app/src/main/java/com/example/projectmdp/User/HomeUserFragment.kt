package com.example.projectmdp.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projectmdp.R
import com.example.projectmdp.databinding.FragmentHomeUserBinding
import com.example.projectmdp.utils.SessionManager

class HomeUserFragment : Fragment() {

    private var _binding: FragmentHomeUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = SessionManager.getUserName(requireContext())

        if (!userName.isNullOrEmpty()) {
            binding.tvWelcomeMessage.text = "Selamat Datang, $userName!"
        } else {
            binding.tvWelcomeMessage.text = "Selamat Datang, User!"
        }


        binding.btnLogout.setOnClickListener {
            // Hapus sesi (token dan nama)
            SessionManager.clearSession(requireContext())
            // Kembali ke halaman login
            findNavController().navigate(R.id.action_homeUserFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}