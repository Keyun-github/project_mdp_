// com/example/projectmdp/User/AddDonationFragment.kt
package com.example.projectmdp.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.projectmdp.databinding.FragmentAddDonationBinding

class AddDonationFragment : Fragment() {

    private var _binding: FragmentAddDonationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDonationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar sebagai ActionBar
        setupToolbar()
    }

    private fun setupToolbar() {
        // Kita perlu mengubah activity menjadi AppCompatActivity untuk bisa mengatur ActionBar
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbarAddDonation)

        // Hubungkan NavController dengan Toolbar.
        // Ini akan secara otomatis menampilkan tombol back (up button) dan
        // menangani aksinya untuk kembali ke fragment sebelumnya.
        NavigationUI.setupWithNavController(binding.toolbarAddDonation, findNavController())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}