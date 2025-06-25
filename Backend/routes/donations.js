const express = require('express');
const router = express.Router();
const Donation = require('../Models/Donation');
const authMiddleware = require('../middleware/auth'); // Impor middleware

// GET - Ambil semua donasi (untuk ditampilkan di home)
router.get('/', async (req, res) => {
  try {
    const donations = await Donation.find().sort({ createdAt: -1 }); // Tampilkan yang terbaru di atas
    res.json(donations);
  } catch (err) {
    res.status(500).json({ message: 'Gagal mengambil data donasi' });
  }
});

// POST - Buat donasi baru (memerlukan login)
router.post('/', authMiddleware, async (req, res) => {
  const { title, targetAmount, creatorName } = req.body;
  
  if (!title || !targetAmount || !creatorName) {
    return res.status(400).json({ message: 'Judul, target donasi, dan nama pembuat wajib diisi.' });
  }

  const newDonation = new Donation({
    title,
    targetAmount,
    creatorName,
    creatorId: req.user.userId // Ambil ID dari token yang sudah di-decode
  });

  try {
    const savedDonation = await newDonation.save();
    res.status(201).json(savedDonation);
  } catch (err) {
    res.status(500).json({ message: 'Gagal menyimpan donasi' });
  }
});

module.exports = router;