const express = require('express');
const router = express.Router();
const Donation = require('../Models/Donation');
const Transaction = require('../Models/Transaction');
const authMiddleware = require('../middleware/auth');

// GET / - Ambil semua donasi (hanya yang aktif)
router.get('/', async (req, res) => {
  try {
    const donations = await Donation.find({ isActive: true }).sort({ createdAt: -1 });
    res.json(donations);
  } catch (err) {
    res.status(500).json({ message: 'Gagal mengambil data donasi' });
  }
});

// GET /:id - Ambil detail satu donasi
router.get('/:id', async (req, res) => {
  try {
    const donation = await Donation.findById(req.params.id);
    if (!donation) return res.status(404).json({ message: 'Donasi tidak ditemukan' });
    res.json(donation);
  } catch (err) {
    res.status(500).json({ message: 'Gagal mengambil data donasi' });
  }
});

// POST / - Buat donasi baru
router.post('/', authMiddleware, async (req, res) => {
  const { title, targetAmount, creatorName } = req.body;
  
  if (!title || !targetAmount || !creatorName) {
    return res.status(400).json({ message: 'Judul, target donasi, dan nama pembuat wajib diisi.' });
  }

  const newDonation = new Donation({
    title,
    targetAmount,
    creatorName,
    creatorId: req.user.userId
  });

  try {
    const savedDonation = await newDonation.save();
    res.status(201).json(savedDonation);
  } catch (err) {
    res.status(500).json({ message: 'Gagal menyimpan donasi' });
  }
});

// POST /:id/donate - Membuat transaksi donasi baru
router.post('/:id/donate', authMiddleware, async (req, res) => {
  try {
    const donation = await Donation.findById(req.params.id);
    if (!donation) return res.status(404).json({ message: 'Donasi tidak ditemukan' });
    if (!donation.isActive) return res.status(400).json({ message: 'Penggalangan dana ini sudah ditutup' });

    const { amount, donatorName } = req.body;
    if (!amount || amount <= 0) {
      return res.status(400).json({ message: 'Jumlah donasi tidak valid' });
    }

    const newTransaction = new Transaction({
      donationId: req.params.id,
      donatorId: req.user.userId,
      donatorName,
      amount
    });
    await newTransaction.save();

    donation.currentAmount += amount;
    const updatedDonation = await donation.save();
    res.json(updatedDonation);
  } catch (err) {
    console.error(err)
    res.status(500).json({ message: 'Gagal melakukan donasi' });
  }
});

// PUT /:id/stop - Menghentikan penggalangan dana
router.put('/:id/stop', authMiddleware, async (req, res) => {
  try {
    const donation = await Donation.findById(req.params.id);
    if (!donation) return res.status(404).json({ message: 'Donasi tidak ditemukan' });
    
    if (donation.creatorId.toString() !== req.user.userId) {
      return res.status(403).json({ message: 'Anda tidak berwenang menghentikan donasi ini' });
    }

    donation.isActive = false;
    const updatedDonation = await donation.save();
    res.json({ message: 'Penggalangan dana berhasil dihentikan', donation: updatedDonation });
  } catch (err) {
    res.status(500).json({ message: 'Gagal menghentikan donasi' });
  }
});

module.exports = router;