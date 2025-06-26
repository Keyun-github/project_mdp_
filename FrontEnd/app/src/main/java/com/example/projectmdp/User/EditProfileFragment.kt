package com.example.projectmdp.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.projectmdp.Login.ApiResult
import com.example.projectmdp.Login.AuthViewModel
import com.example.projectmdp.Login.AuthViewModelFactory
import com.example.projectmdp.api.UpdateProfileRequest
import com.example.projectmdp.databinding.FragmentEditProfileBinding
import com.example.projectmdp.utils.SessionManager

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        // Isi nama lengkap yang ada saat ini dari SessionManager
        binding.etEditNama.setText(SessionManager.getUserName(requireContext()))

        binding.btnSaveProfile.setOnClickListener { handleSaveProfile() }

        observeViewModel()
    }

    private fun handleSaveProfile() {
        val namaLengkap = binding.etEditNama.text.toString().trim()
        val oldPassword = binding.etOldPassword.text.toString()
        val newPassword = binding.etNewPassword.text.toString()

        if (namaLengkap.isEmpty()) {
            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        // Validasi jika pengguna ingin mengganti password
        if (newPassword.isNotEmpty() && oldPassword.isEmpty()) {
            Toast.makeText(context, "Masukkan password lama untuk mengganti password", Toast.LENGTH_SHORT).show()
            return
        }

        val request = UpdateProfileRequest(
            namaLengkap = namaLengkap,
            oldPassword = if (oldPassword.isNotEmpty()) oldPassword else null,
            newPassword = if (newPassword.isNotEmpty()) newPassword else null
        )

        authViewModel.updateUserProfile(request)
    }

    private fun observeViewModel() {
        authViewModel.updateProfileResult.observe(viewLifecycleOwner) { result ->
            // Mengatur visibilitas tombol berdasarkan state
            binding.btnSaveProfile.isEnabled = result !is ApiResult.Loading

            when(result) {
                is ApiResult.Success -> {
                    Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    // Update nama di SessionManager dengan data baru dari server
                    SessionManager.saveUserName(requireContext(), result.data.user.namaLengkap)
                    // Kembali ke halaman profil
                    findNavController().popBackStack()
                }
                is ApiResult.Error -> {
                    val errorMsg = try { org.json.JSONObject(result.message).getString("message") }
                    catch(e: Exception) { result.message }
                    Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                }
                is ApiResult.Loading -> {
                    // Opcional: Tampilkan loading state, misal di tombol
                    binding.btnSaveProfile.text = "Menyimpan..."
                }
            }
            // Kembalikan teks tombol setelah proses selesai (bukan loading)
            if(result !is ApiResult.Loading) {
                binding.btnSaveProfile.text = "Simpan Perubahan"
            }
        }
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbarEditProfile)
        NavigationUI.setupWithNavController(binding.toolbarEditProfile, findNavController())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}