package com.example.skillfoliofinal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView

/**
 * AppUtils: Utility singleton untuk fungsi-fungsi yang digunakan di seluruh aplikasi.
 * - Menyediakan fungsi loadProfilePhoto() agar foto profil sinkron di semua layar.
 * - Menyediakan fungsi navigasi dengan smooth fade+scale transition (TANPA SLIDE).
 *
 * Semua transisi menggunakan:
 *   - navigateTo()  → layar baru "pop in" (scale 95%→100% + fade)
 *   - navigateTab() → pure fade (untuk tab switch / bottom nav)
 *   - navigateBack() → layar ini dismiss (scale up + fade out), layar lama fade kembali
 */
object AppUtils {

    /**
     * Load foto profil user ke ImageView yang diberikan.
     * Urutan prioritas: database → SharedPreferences → default avatar.
     */
    fun loadProfilePhoto(context: Context, imageView: ImageView, dbHelper: DatabaseHelper? = null) {
        val prefs = context.getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        val email = prefs.getString("user_email", "") ?: ""

        // Coba ambil dari database dulu (paling akurat)
        val fotoUri: String = if (dbHelper != null && email.isNotEmpty()) {
            dbHelper.getUserByEmail(email)?.fotoUri
                ?: prefs.getString("user_foto_uri", "") ?: ""
        } else {
            prefs.getString("user_foto_uri", "") ?: ""
        }

        if (fotoUri.isNotEmpty()) {
            try {
                imageView.setImageURI(Uri.parse(fotoUri))
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            } catch (e: Exception) {
                imageView.setImageResource(R.drawable.ic_avatar)
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        } else {
            imageView.setImageResource(R.drawable.ic_avatar)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }

    /**
     * Navigate ke Activity baru — layar baru "pop in" (scale+fade), TANPA SLIDE.
     * Gunakan untuk navigasi maju: buka detail, form tambah, dll.
     */
    fun navigateTo(activity: Activity, intent: Intent, finishCurrent: Boolean = false) {
        activity.startActivity(intent)
        // Masuk: pop-in (scale 95→100 + fade)  |  Keluar: scale-down + fade
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        if (finishCurrent) activity.finish()
    }

    /**
     * Navigate antar tab/halaman utama — pure fade, TANPA SLIDE.
     * Gunakan untuk bottom nav tab switch.
     */
    fun navigateTab(activity: Activity, intent: Intent, finishCurrent: Boolean = true) {
        activity.startActivity(intent)
        // Pure fade — terasa seperti "ganti konten" bukan "pindah layar"
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        if (finishCurrent) activity.finish()
    }

    /**
     * Kembali / dismiss layar — layar ini "pop out" (scale up + fade), layar sebelumnya restore.
     * TANPA SLIDE.
     */
    fun navigateBack(activity: Activity) {
        activity.finish()
        // Layar lama restore (fade+scale 97→100) | Layar ini dismiss (scale 100→103 + fade out)
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
