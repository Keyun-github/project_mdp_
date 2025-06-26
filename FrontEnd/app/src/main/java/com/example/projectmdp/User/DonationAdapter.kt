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

/**
 * Adapter untuk menampilkan daftar donasi di dalam RecyclerView.
 * Menggunakan ListAdapter untuk performa yang efisien.
 *
 * @param onItemClicked Lambda function yang akan dipanggil saat sebuah item di-klik.
 */
class DonationAdapter(private val onItemClicked: (Donation) -> Unit) :
    ListAdapter<Donation, DonationAdapter.DonationViewHolder>(DonationDiffCallback()) {

    /**
     * ViewHolder bertanggung jawab untuk memegang referensi ke view dari setiap item
     * dan mengisi data (binding) ke dalam view tersebut.
     */
    class DonationViewHolder(private val binding: DonationListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        // Formatter untuk mengubah angka menjadi format mata uang Rupiah (Rp 10.000)
        private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
            maximumFractionDigits = 0
        }

        fun bind(donation: Donation) {
            binding.tvDonationTitle.text = donation.title
            binding.tvCreator.text = "dibuat oleh: ${donation.creatorName}"

            // Format angka ke dalam string mata uang
            val currentFormatted = currencyFormatter.format(donation.currentAmount)
            val targetFormatted = currencyFormatter.format(donation.targetAmount)
            binding.tvDonationAmount.text = "$currentFormatted / $targetFormatted"

            // Mengatur nilai maksimum dan progress dari ProgressBar
            binding.pbDonationProgress.max = donation.targetAmount.toInt()
            binding.pbDonationProgress.progress = donation.currentAmount.toInt()
        }
    }

    /**
     * Membuat ViewHolder baru saat RecyclerView membutuhkannya.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val binding = DonationListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DonationViewHolder(binding)
    }

    /**
     * Mengikat data dari objek Donation ke ViewHolder pada posisi tertentu.
     * Juga mengatur OnClickListener untuk setiap item.
     */
    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val currentDonation = getItem(position)

        // Panggil fungsi bind di ViewHolder untuk mengisi data
        holder.bind(currentDonation)

        // Atur listener klik pada view item
        holder.itemView.setOnClickListener {
            onItemClicked(currentDonation)
        }
    }

    /**
     * DiffUtil.ItemCallback membantu ListAdapter untuk mengetahui item mana
     * yang berubah, ditambahkan, atau dihapus, sehingga animasi RecyclerView
     * bisa berjalan dengan efisien.
     */
    class DonationDiffCallback : DiffUtil.ItemCallback<Donation>() {
        override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            // Item dianggap sama jika ID uniknya sama.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
            // Konten dianggap sama jika semua properti di data class sama.
            return oldItem == newItem
        }
    }
}