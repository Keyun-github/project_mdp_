const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
require('dotenv').config();

const authRoutes = require('./routes/auth');
const donationRoutes = require('./routes/donations');

const app = express();

app.use(cors());
app.use(express.json());

app.use('/api/auth', authRoutes);
app.use('/api/donations', donationRoutes);

if (!process.env.MONGO_URI) {
  console.error('FATAL ERROR: MONGO_URI tidak ditemukan di file .env');
  process.exit(1);
}
if (!process.env.JWT_SECRET) {
  console.error('FATAL ERROR: JWT_SECRET tidak ditemukan di file .env');
  process.exit(1);
}

// Definisikan Port dan Host
const PORT = process.env.PORT || 3000;
const HOST = '0.0.0.0'; // Mengikat ke semua antarmuka jaringan yang tersedia

// Koneksi ke MongoDB dan jalankan server
mongoose.connect(process.env.MONGO_URI)
  .then(() => {
    console.log('MongoDB connected successfully.');
    
    app.listen(PORT, HOST, () => {
      // Pesan log ini lebih membantu untuk development di jaringan lokal
      console.log(`Server is running and listening on host: ${HOST}, port: ${PORT}`);
      console.log('You can now connect from your Android device or other devices on the same network.');
    });
  })
  .catch(err => {
    console.error('MongoDB connection error:', err.message);
    process.exit(1); // Keluar dari aplikasi jika koneksi database gagal
  });