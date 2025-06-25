package com.example.projectmdp.User

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmdp.api.Donation
import com.example.projectmdp.databinding.DonationListItemBinding
import java.text.NumberFormat
import java.util.Locale

class DonationAdapter : ListAdapter<Donation, DonationAdapter.DonationViewHolder>(DonationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val binding = DonationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DonationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val donation = getItem(position)
        holder.bind(donation)
    }

    class DonationViewHolder(private val binding: DonationListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(donation: Donation) {
            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            formatter.maximumFractionDigits = 0

            binding.tvDonationTitle.text = donation.title
            binding.tvCreator.text = "dibuat oleh: ${donation.creatorName}"

            val currentFormatted = formatter.format(donation.currentAmount)
            val targetFormatted = formatter.format(donation.targetAmount)
            binding.tvDonationAmount.text = "$currentFormatted / $targetFormatted"

            binding.pbDonationProgress.max = donation.targetAmount.toInt()
            binding.pbDonationProgress.progress = donation.currentAmount.toInt()
        }
    }

    class DonationDiffCallback : DiffUtil.ItemCallback<Donation>() {
        override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            return oldItem == newItem
        }
    }
}