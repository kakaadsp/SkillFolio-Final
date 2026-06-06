package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class PengaturanActivity : AppCompatActivity() {

    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvBahasa: TextView
    private lateinit var tvLokasi: TextView
    private lateinit var switchNotifikasi: SwitchCompat
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        tvNama = findViewById(R.id.tv_nama)
        tvEmail = findViewById(R.id.tv_email)
        tvBahasa = findViewById(R.id.tv_bahasa_value)
        tvLokasi = findViewById(R.id.tv_lokasi_value)
        switchNotifikasi = findViewById(R.id.switch_notifikasi)
        bottomNav = findViewById(R.id.bottom_nav)

        // Load details and settings from SharedPreferences
        val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        tvNama.text = prefs.getString("user_nama", "Pengguna")
        tvEmail.text = prefs.getString("user_email", "email@example.com")
        
        tvBahasa.text = prefs.getString("pref_language", "Indonesia")
        tvLokasi.text = prefs.getString("pref_location", "Surabaya, Jawa Timur")
        switchNotifikasi.isChecked = prefs.getBoolean("pref_notifications", true)

        // Click listeners for Bahasa, Notifikasi, and Lokasi
        findViewById<LinearLayout>(R.id.row_bahasa).setOnClickListener {
            showLanguageDialog(prefs)
        }

        switchNotifikasi.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("pref_notifications", isChecked).apply()
            val message = if (isChecked) "Notifikasi diaktifkan" else "Notifikasi dinonaktifkan"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.row_lokasi).setOnClickListener {
            showLocationDialog(prefs)
        }

        findViewById<LinearLayout>(R.id.row_ubah_sandi).setOnClickListener {
            Toast.makeText(this, "Fitur segera hadir", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.row_privasi_data).setOnClickListener {
            Toast.makeText(this, "Fitur segera hadir", Toast.LENGTH_SHORT).show()
        }

        // Setup bottom nav navigation listeners
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    AppUtils.navigateTab(this, intent, finishCurrent = true)
                    false
                }
                R.id.navigation_experience -> {
                    AppUtils.navigateTab(this, Intent(this, ListPengalamanActivity::class.java))
                    false
                }
                R.id.navigation_projects -> {
                    AppUtils.navigateTab(this, Intent(this, ListProjectActivity::class.java))
                    false
                }
                R.id.navigation_profile -> {
                    // Navigate back to main ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    AppUtils.navigateTab(this, intent, finishCurrent = true)
                    false
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload details in case name or settings were changed
        val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        tvNama.text = prefs.getString("user_nama", "Pengguna")
        tvEmail.text = prefs.getString("user_email", "email@example.com")
        tvBahasa.text = prefs.getString("pref_language", "Indonesia")
        tvLokasi.text = prefs.getString("pref_location", "Surabaya, Jawa Timur")
        switchNotifikasi.isChecked = prefs.getBoolean("pref_notifications", true)
    }

    private fun showLanguageDialog(prefs: SharedPreferences) {
        val languages = arrayOf("Indonesia", "English")
        val currentLanguage = prefs.getString("pref_language", "Indonesia")
        val checkedItem = languages.indexOf(currentLanguage).let { if (it == -1) 0 else it }

        AlertDialog.Builder(this)
            .setTitle("Pilih Bahasa")
            .setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                val selectedLang = languages[which]
                prefs.edit().putString("pref_language", selectedLang).apply()
                tvBahasa.text = selectedLang
                Toast.makeText(this, "Bahasa diubah ke $selectedLang", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showLocationDialog(prefs: SharedPreferences) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ubah Lokasi")

        val input = EditText(this)
        input.setText(tvLokasi.text)
        input.setSingleLine(true)

        val paddingPx = (16 * resources.displayMetrics.density).toInt()
        val container = FrameLayout(this)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.leftMargin = paddingPx
        params.rightMargin = paddingPx
        input.layoutParams = params
        container.addView(input)
        builder.setView(container)

        builder.setPositiveButton("Simpan") { _, _ ->
            val newLocation = input.text.toString().trim()
            if (newLocation.isNotEmpty()) {
                prefs.edit().putString("pref_location", newLocation).apply()
                tvLokasi.text = newLocation
                Toast.makeText(this, "Lokasi diperbarui!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Lokasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Batal", null)
        builder.show()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
