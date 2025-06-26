const mongoose = require('mongoose');

const donationSchema = new mongoose.Schema({
  title: { type: String, required: true },
  targetAmount: { type: Number, required: true },
  currentAmount: { type: Number, default: 0 },
  creatorName: { type: String, required: true },
  creatorId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  isActive: { type: Boolean, default: true } // Field baru untuk status donasi
}, { timestamps: true });

module.exports = mongoose.models.Donation || mongoose.model('Donation', donationSchema);