const jwt = require('jsonwebtoken');

module.exports = function(req, res, next) {
  const token = req.header('Authorization')?.replace('Bearer ', '');
  if (!token) return res.status(401).json({ message: 'Akses ditolak. Tidak ada token.' });

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.user = decoded; // Menambahkan payload token (userId, role) ke request
    next();
  } catch (ex) {
    res.status(400).json({ message: 'Token tidak valid.' });
  }
};