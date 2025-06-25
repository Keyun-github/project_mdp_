package com.example.projectmdp.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projectmdp.R
import com.example.projectmdp.databinding.FragmentHomeContentBinding

class HomeContentFragment : Fragment() {

    private var _binding: FragmentHomeContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup listener untuk FloatingActionButton
        binding.fabAddDonation.setOnClickListener {
            // Gunakan action yang sudah kita definisikan di nav graph
            findNavController().navigate(R.id.action_homeContentFragment_to_addDonationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}