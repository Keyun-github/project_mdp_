// models/User.js
const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  namaLengkap: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  role: { type: String, enum: ['admin', 'organisasi', 'donatur'], default: 'donatur' },
  customId: { type: String, unique: true }
});

module.exports = mongoose.models.User || mongoose.model('User', userSchema);