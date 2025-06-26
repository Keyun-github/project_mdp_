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

    // Inisialisasi ViewModel menggunakan Factory
    private val viewModel: DonationViewModel by viewModels {
        DonationViewModelFactory(requireContext())
    }

    // Deklarasi adapter untuk RecyclerView
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

    /**
     * Fungsi ini dipanggil setiap kali fragment kembali terlihat oleh pengguna.
     * Sangat baik untuk memuat ulang data di sini agar selalu up-to-date,
     * misalnya setelah pengguna membuat donasi baru dan kembali ke halaman ini.
     */
    override fun onResume() {
        super.onResume()
        viewModel.fetchAllDonations()
    }

    /**
     * Menyiapkan RecyclerView dan Adapternya.
     * Termasuk menangani aksi klik pada setiap item.
     */
    private fun setupRecyclerView() {
        // Inisialisasi adapter dengan lambda function untuk menangani klik item
        donationAdapter = DonationAdapter { donation ->
            // Membuat action untuk navigasi ke DonationDetailFragment
            // dengan membawa ID donasi yang diklik sebagai argumen.
            val action = HomeContentFragmentDirections.actionHomeContentFragmentToDonationDetailFragment(donation.id)
            findNavController().navigate(action)
        }

        binding.rvDonations.apply {
            adapter = donationAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Menyiapkan semua listener untuk view di dalam fragment ini.
     */
    private fun setupListeners() {
        // Setup listener untuk FloatingActionButton (tombol +)
        binding.fabAddDonation.setOnClickListener {
            // Gunakan action yang sudah didefinisikan di home_nav_graph.xml
            findNavController().navigate(R.id.action_homeContentFragment_to_addDonationFragment)
        }
    }

    /**
     * Mengamati LiveData dari ViewModel untuk memperbarui UI secara reaktif.
     */
    private fun observeViewModel() {
        viewModel.donations.observe(viewLifecycleOwner) { result ->
            // Mengatur visibilitas ProgressBar berdasarkan state loading
            // Anda perlu menambahkan ProgressBar dengan id 'progress_bar_home' di layout
            // binding.progressBarHome.isVisible = result is ApiResult.Loading

            when (result) {
                is ApiResult.Success -> {
                    // Jika data berhasil didapat, kirim ke adapter
                    binding.rvDonations.isVisible = true
                    donationAdapter.submitList(result.data)
                }
                is ApiResult.Error -> {
                    // Jika terjadi error, tampilkan pesan
                    binding.rvDonations.isVisible = true // Bisa jadi tetap menampilkan data lama
                    Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
                // Kasus Loading sudah ditangani di atas
                is ApiResult.Loading -> {
                    binding.rvDonations.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}