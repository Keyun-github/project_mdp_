const express = require('express');
const router = express.Router();
const User = require('../models/User');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const authMiddleware = require('../middleware/auth'); // Impor middleware di sini

// ===================================
//   ROUTE: POST /api/auth/register
// ===================================
router.post('/register', async (req, res) => {
  const { namaLengkap, email, password, confirmPassword } = req.body;

  if (!namaLengkap || !email || !password || !confirmPassword) {
    return res.status(400).json({ message: 'Harap lengkapi semua data' });
  }

  if (password !== confirmPassword) {
    return res.status(400).json({ message: 'Password dan konfirmasi tidak cocok' });
  }

  try {
    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ message: 'Email sudah terdaftar' });
    }

    let role = 'donatur';
    if (email === 'master@gmail.com' && password === '12345678') {
      role = 'admin';
    } else if (email === 'pengalang@gmail.com' && password === '12345678') {
      role = 'organisasi';
    }

    const rolePrefix = role === 'donatur' ? 'D' : role === 'organisasi' ? 'O' : 'A';
    const count = await User.countDocuments({ role });
    const nextNumber = (count + 1).toString().padStart(3, '0');
    const customId = `${rolePrefix}${nextNumber}`;

    const hashedPassword = await bcrypt.hash(password, 10);

    const newUser = new User({
      namaLengkap,
      email,
      password: hashedPassword,
      role,
      customId
    });

    await newUser.save();
    res.status(201).json({ message: 'Registrasi berhasil' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Terjadi kesalahan server' });
  }
});

// ===================================
//   ROUTE: POST /api/auth/login
// ===================================
router.post('/login', async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ message: 'Email dan password wajib diisi' });
  }

  try {
    const user = await User.findOne({ email });
    if (!user) {
      return res.status(400).json({ message: 'Email tidak ditemukan' });
    }

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) {
      return res.status(400).json({ message: 'Password salah' });
    }

    const token = jwt.sign({ userId: user._id, role: user.role }, process.env.JWT_SECRET, {
      expiresIn: '1d'
    });

    res.json({
      message: 'Login berhasil',
      token,
      user: {
        id: user._id,
        namaLengkap: user.namaLengkap,
        email: user.email,
        role: user.role,
        customId: user.customId
      }
    });
  } catch (err) {
    res.status(500).json({ message: 'Terjadi kesalahan server' });
  }
});

// ===================================
//   ROUTE: PUT /api/auth/profile
// ===================================
router.put('/profile', authMiddleware, async (req, res) => {
  const { namaLengkap, oldPassword, newPassword } = req.body;
  const userId = req.user.userId;

  try {
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ message: 'Pengguna tidak ditemukan' });
    }

    if (namaLengkap) {
      user.namaLengkap = namaLengkap;
    }

    if (newPassword && newPassword.trim() !== '') {
      if (!oldPassword) {
        return res.status(400).json({ message: 'Password lama wajib diisi untuk mengganti password' });
      }

      const isMatch = await bcrypt.compare(oldPassword, user.password);
      if (!isMatch) {
        return res.status(400).json({ message: 'Password lama salah' });
      }

      user.password = await bcrypt.hash(newPassword, 10);
    }

    const updatedUser = await user.save();

    res.json({
      message: 'Profil berhasil diperbarui',
      user: {
        id: updatedUser._id,
        namaLengkap: updatedUser.namaLengkap,
        email: updatedUser.email,
        role: updatedUser.role,
        customId: updatedUser.customId
      }
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Terjadi kesalahan server' });
  }
});

module.exports = router;