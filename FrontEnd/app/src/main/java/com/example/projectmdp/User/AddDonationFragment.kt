package com.example.projectmdp.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.projectmdp.Login.ApiResult
import com.example.projectmdp.databinding.FragmentAddDonationBinding
import com.example.projectmdp.api.CreateDonationRequest
import com.example.projectmdp.utils.DonationValidationUtils
import com.example.projectmdp.utils.SessionManager

class AddDonationFragment : Fragment() {

    private var _binding: FragmentAddDonationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DonationViewModel by viewModels {
        DonationViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDonationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        binding.btnSaveDonation.setOnClickListener {
            val title = binding.etDonationTitle.text.toString().trim()
            val targetStr = binding.etDonationTarget.text.toString().trim()
            val creatorName = SessionManager.getUserName(requireContext()) ?: "Anonim"

            val errorMessage = DonationValidationUtils.validateDonationInput(title, targetStr)
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = CreateDonationRequest(title, targetStr.toLong(), creatorName)
            viewModel.addNewDonation(request)

        }

        viewModel.createStatus.observe(viewLifecycleOwner) { result ->
            when(result) {
                is ApiResult.Success -> {
                    Toast.makeText(context, "Donasi berhasil dibuat!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is ApiResult.Error -> Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                ApiResult.Loading -> { /* Show loading indicator */ }
            }
        }
    }
    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbarAddDonation)

        NavigationUI.setupWithNavController(binding.toolbarAddDonation, findNavController())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}