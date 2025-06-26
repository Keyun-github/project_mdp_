const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
require('dotenv').config();

const authRoutes = require('./routes/auth');
const donationRoutes = require('./routes/donations'); // Impor route donasi

const app = express();

app.use(cors());
app.use(express.json());

// Menggunakan routes
app.use('/api/auth', authRoutes);
app.use('/api/donations', donationRoutes); // Gunakan route donasi

// ... (Cek variabel .env) ...

const PORT = process.env.PORT || 3000;
const HOST = '0.0.0.0';

mongoose.connect(process.env.MONGO_URI)
  .then(() => {
    console.log('MongoDB connected successfully.');
    app.listen(PORT, HOST, () => {
      console.log(`Server is running and listening on host: ${HOST}, port: ${PORT}`);
    });
  })
  .catch(err => {
    console.error('MongoDB connection error:', err.message);
    process.exit(1);
  });