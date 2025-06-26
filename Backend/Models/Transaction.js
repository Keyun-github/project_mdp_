const mongoose = require('mongoose');

const transactionSchema = new mongoose.Schema({
  donationId: { type: mongoose.Schema.Types.ObjectId, ref: 'Donation', required: true },
  donatorId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  donatorName: { type: String, required: true },
  amount: { type: Number, required: true }
}, { timestamps: true });

module.exports = mongoose.model('Transaction', transactionSchema);