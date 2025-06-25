package com.example.projectmdp.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmdp.Login.ApiResult
import com.example.projectmdp.R
import com.example.projectmdp.databinding.FragmentHomeContentBinding

class HomeContentFragment : Fragment() {

    private var _binding: FragmentHomeContentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DonationViewModel by viewModels {
        DonationViewModelFactory(requireContext())
    }

    private lateinit var donationAdapter: DonationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAllDonations()
    }

    private fun setupRecyclerView() {
        donationAdapter = DonationAdapter()
        binding.rvDonations.apply {
            adapter = donationAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupListeners() {
        binding.fabAddDonation.setOnClickListener {
            findNavController().navigate(R.id.action_homeContentFragment_to_addDonationFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.donations.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Loading -> {
                    binding.rvDonations.isVisible = false
                }
                is ApiResult.Success -> {
                    binding.rvDonations.isVisible = true
                    donationAdapter.submitList(result.data)
                }
                is ApiResult.Error -> {
                    binding.rvDonations.isVisible = true
                    Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}