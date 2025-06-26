package com.example.projectmdp.User

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.projectmdp.Login.ApiResult
import com.example.projectmdp.api.Donation
import com.example.projectmdp.api.MakeDonationRequest
import com.example.projectmdp.databinding.FragmentDonationDetailBinding
import com.example.projectmdp.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class DonationDetailFragment : Fragment() {

    private var _binding: FragmentDonationDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DonationDetailFragmentArgs by navArgs()
    private val viewModel: DonationViewModel by viewModels { DonationViewModelFactory(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDonationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        viewModel.fetchDonationDetail(args.donationId)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.donationDetail.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result is ApiResult.Loading

            if (result is ApiResult.Success) {
                val donation = result.data
                bindDataToView(donation)
                setupActionButton(donation)
            } else if (result is ApiResult.Error) {
                Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.transactionStatus.observe(viewLifecycleOwner) { result ->
            val isLoading = result is ApiResult.Loading
            binding.btnAction.isEnabled = !isLoading
            if (isLoading) binding.btnAction.text = "Memproses..."

            if (result is ApiResult.Success) {
                Toast.makeText(context, "Donasi berhasil!", Toast.LENGTH_SHORT).show()
                // Data akan direfresh otomatis oleh fetchDonationDetail di dalam ViewModel
            } else if (result is ApiResult.Error) {
                Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.stopStatus.observe(viewLifecycleOwner) { result ->
            val isLoading = result is ApiResult.Loading
            binding.btnAction.isEnabled = !isLoading
            if (isLoading) binding.btnAction.text = "Memproses..."

            if (result is ApiResult.Success) {
                Toast.makeText(context, result.data.message, Toast.LENGTH_SHORT).show()
            } else if (result is ApiResult.Error) {
                Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindDataToView(donation: Donation) {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply { maximumFractionDigits = 0 }

        binding.tvDetailTitle.text = donation.title
        binding.tvDetailCreator.text = "oleh ${donation.creatorName}"
        binding.pbDetailProgress.max = donation.targetAmount.toInt()
        binding.pbDetailProgress.progress = donation.currentAmount.toInt()

        val current = formatter.format(donation.currentAmount)
        val target = formatter.format(donation.targetAmount)
        binding.tvDetailAmount.text = "$current terkumpul dari $target"

        binding.tvDonationStatus.isVisible = !donation.isActive
    }

    private fun setupActionButton(donation: Donation) {
        val currentUserId = SessionManager.getUserId(requireContext())

        binding.btnAction.isEnabled = donation.isActive

        if (donation.creatorId == currentUserId) {
            binding.btnAction.text = "Hentikan Penggalangan Dana"
            binding.btnAction.setOnClickListener {
                showStopConfirmationDialog(donation.id)
            }
        } else {
            binding.btnAction.text = "Berikan Donasi"
            binding.btnAction.setOnClickListener { showDonatePopup(donation.id) }
        }
    }

    private fun showStopConfirmationDialog(donationId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hentikan Donasi")
            .setMessage("Apakah Anda yakin ingin menghentikan penggalangan dana ini? Aksi ini tidak dapat dibatalkan.")
            .setPositiveButton("Ya, Hentikan") { _, _ ->
                viewModel.stopDonation(donationId)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDonatePopup(donationId: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Masukkan Jumlah Donasi")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.hint = "Contoh: 50000"
        builder.setView(input)

        builder.setPositiveButton("Donasi") { dialog, _ ->
            val amountStr = input.text.toString()
            if (amountStr.isNotEmpty()) {
                val amount = amountStr.toLong()
                val donatorName = SessionManager.getUserName(requireContext()) ?: "Donatur Baik"
                val request = MakeDonationRequest(amount, donatorName)
                viewModel.submitDonation(donationId, request)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Jumlah tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbarDonationDetail)
        NavigationUI.setupWithNavController(binding.toolbarDonationDetail, findNavController())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}