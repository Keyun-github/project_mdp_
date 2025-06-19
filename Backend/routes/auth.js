const express = require('express');
const router = express.Router();
const User = require('../Models/User');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

// REGISTER
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
    if (existingUser)
      return res.status(400).json({ message: 'Email sudah terdaftar' });

    // Tentukan role
    let role = 'donatur';
    if (email === 'master@gmail.com' && password === '12345678') {
      role = 'admin';
    } else if (email === 'pengalang@gmail.com' && password === '12345678') {
      role = 'organisasi';
    }

    // Ambil jumlah user sesuai role
    const rolePrefix = role === 'donatur' ? 'D' : role === 'organisasi' ? 'O' : 'A';
    const count = await User.countDocuments({ role });
    const nextNumber = (count + 1).toString().padStart(3, '0'); // 001, 002
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


// LOGIN
router.post('/login', async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password)
    return res.status(400).json({ message: 'Email dan password wajib diisi' });

  try {
    const user = await User.findOne({ email });
    if (!user)
      return res.status(400).json({ message: 'Email tidak ditemukan' });

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch)
      return res.status(400).json({ message: 'Password salah' });

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
        role: user.role
      }
    });
  } catch (err) {
    res.status(500).json({ message: 'Terjadi kesalahan server' });
  }
});
//get
router.get('/users', async (req, res) => {
  try {
    const users = await User.find({}, '-password'); // sembunyikan password
    res.json(users);
  } catch (err) {
    res.status(500).json({ message: 'Gagal mengambil data user' });
  }
});
//update 
router.put('/users/:id', async (req, res) => {
  const { namaLengkap, email, role } = req.body;

  try {
    const updatedUser = await User.findByIdAndUpdate(
      req.params.id,
      { namaLengkap, email, role },
      { new: true }
    );
    res.json(updatedUser);
  } catch (err) {
    res.status(500).json({ message: 'Gagal mengupdate user' });
  }
});

//delete
router.delete('/users/:id', async (req, res) => {
  try {
    await User.findByIdAndDelete(req.params.id);
    res.json({ message: 'User berhasil dihapus' });
  } catch (err) {
    res.status(500).json({ message: 'Gagal menghapus user' });
  }
});

module.exports = router;
