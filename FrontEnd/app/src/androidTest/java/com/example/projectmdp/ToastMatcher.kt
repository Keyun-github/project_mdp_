package com.example.projectmdp
import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Custom Matcher untuk memeriksa apakah sebuah Root view adalah Toast.
 * Espresso tidak bisa langsung menemukan Toast karena Toast berjalan di window-nya sendiri,
 * terpisah dari window utama aplikasi. Matcher ini membantu mengatasinya.
 */
class ToastMatcher : TypeSafeMatcher<Root>() {

    // Memberikan deskripsi jika test gagal, misalnya "Expected: is toast"
    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    // Logika utama untuk mencocokkan Toast
    override fun matchesSafely(item: Root): Boolean {
        // Mendapatkan tipe window dari Root view
        val type: Int = item.windowLayoutParams.get().type

        // Toast memiliki tipe window khusus: TYPE_TOAST
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken: IBinder = item.decorView.windowToken
            val appToken: IBinder = item.decorView.applicationWindowToken
            // Memastikan Toast ini benar-benar milik aplikasi yang sedang diuji,
            // bukan dari sistem atau aplikasi lain.
            if (windowToken === appToken) {
                return true
            }
        }
        return false
    }
}