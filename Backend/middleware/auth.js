const jwt = require('jsonwebtoken');

/**
 * Middleware untuk memverifikasi token JWT.
 * Fungsi ini akan mengambil token dari header Authorization,
 * memverifikasinya, dan jika valid, akan menambahkan payload user
 * ke dalam objek request (req.user).
 */
function authMiddleware(req, res, next) {
  // 1. Ambil token dari header 'Authorization'
  const authHeader = req.header('Authorization');

  // 2. Cek apakah header ada dan formatnya benar ("Bearer <token>")
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ message: 'Akses ditolak. Format token salah atau tidak ada.' });
  }

  // 3. Ambil token saja, tanpa prefix "Bearer "
  const token = authHeader.substring(7);

  try {
    // 4. Verifikasi token menggunakan secret key dari .env
    const decodedPayload = jwt.verify(token, process.env.JWT_SECRET);

    // 5. Jika token valid, tambahkan payload (yang berisi userId, role, dll.)
    //    ke objek request agar bisa digunakan oleh route handler selanjutnya.
    req.user = decodedPayload;

    // 6. Lanjutkan ke proses berikutnya (route handler)
    next();
  } catch (ex) {
    // 7. Jika token tidak valid (kadaluarsa, salah, dll.), kirim error.
    res.status(400).json({ message: 'Token tidak valid atau sudah kadaluarsa.' });
  }
}

module.exports = authMiddleware;