package com.example.projectmdp.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmdp.ai.AIChatViewModel
import com.example.projectmdp.ai.AiResult
import com.example.projectmdp.ai.ChatAdapter
import com.example.projectmdp.ai.ChatMessage
import com.example.projectmdp.databinding.FragmentAiBinding

class AIFragment : Fragment() {

    private var _binding: FragmentAiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AIChatViewModel by viewModels()
    private val chatMessages = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.btnSend.setOnClickListener {
            val prompt = binding.etPrompt.text.toString().trim()
            if (prompt.isNotEmpty()) {
                sendMessage(prompt)
            }
        }

        observeViewModel()
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(chatMessages)
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true // Pesan baru akan muncul di bawah dan mendorong ke atas
            }
        }
    }

    private fun sendMessage(prompt: String) {
        // Tambahkan pesan pengguna ke UI segera
        addMessageToList(ChatMessage(prompt, "user"))
        binding.etPrompt.text?.clear()

        // Kirim prompt ke ViewModel untuk diproses oleh AI
        viewModel.sendMessage(prompt)
    }

    private fun observeViewModel() {
        viewModel.chatResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AiResult.Loading -> {
                    binding.pbLoading.isVisible = true
                    binding.btnSend.isEnabled = false
                }
                is AiResult.Success -> {
                    binding.pbLoading.isVisible = false
                    binding.btnSend.isEnabled = true
                    // Tambahkan respons dari AI ke UI
                    addMessageToList(ChatMessage(result.message, "model"))
                }
                is AiResult.Error -> {
                    binding.pbLoading.isVisible = false
                    binding.btnSend.isEnabled = true
                    Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun addMessageToList(message: ChatMessage) {
        chatMessages.add(message)
        chatAdapter.notifyItemInserted(chatMessages.size - 1)
        // Scroll ke posisi terakhir agar pesan baru terlihat
        binding.rvChat.scrollToPosition(chatMessages.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}