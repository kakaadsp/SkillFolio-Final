package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvProdi: TextView
    private lateinit var tvLokasi: TextView
    private lateinit var tvLinkedin: TextView
    private lateinit var tvGithub: TextView
    private lateinit var tvBio: TextView
    private lateinit var ivProfileAvatar: ImageView
    private lateinit var bottomNav: BottomNavigationView

    private lateinit var rowLokasi: LinearLayout
    private lateinit var rowLinkedin: LinearLayout
    private lateinit var rowGithub: LinearLayout
    private lateinit var rowBio: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DatabaseHelper(this)

        tvNama         = findViewById(R.id.tv_nama)
        tvEmail        = findViewById(R.id.tv_email)
        tvPhone        = findViewById(R.id.tv_phone)
        tvProdi        = findViewById(R.id.tv_prodi)
        tvLokasi       = findViewById(R.id.tv_lokasi)
        tvLinkedin     = findViewById(R.id.tv_linkedin)
        tvGithub       = findViewById(R.id.tv_github)
        tvBio          = findViewById(R.id.tv_bio)
        ivProfileAvatar = findViewById(R.id.iv_profile_avatar)
        bottomNav      = findViewById(R.id.bottom_nav)

        rowLokasi      = findViewById(R.id.row_lokasi)
        rowLinkedin    = findViewById(R.id.row_linkedin)
        rowGithub      = findViewById(R.id.row_github)
        rowBio         = findViewById(R.id.row_bio)

        // Menu click listeners with smooth transitions
        findViewById<LinearLayout>(R.id.menu_edit_profil).setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, EditProfilActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.menu_pengaturan).setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, PengaturanActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.menu_keluar).setOnClickListener {
            showLogoutConfirmDialog()
        }

        // ── Template Otomatis card → buka CV Preview ─────────────────────────
        findViewById<CardView>(R.id.card_template_otomatis).setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, CvPreviewActivity::class.java))
        }

        // LinkedIn click → open link
        rowLinkedin.setOnClickListener {
            val url = tvLinkedin.text.toString()
            if (url.isNotEmpty() && url != "-") openUrl(url)
        }
        // GitHub click → open link
        rowGithub.setOnClickListener {
            val url = tvGithub.text.toString()
            if (url.isNotEmpty() && url != "-") openUrl(url)
        }

        // Bottom Navigation
        bottomNav.selectedItemId = R.id.navigation_profile
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
                R.id.navigation_profile -> true
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNav.selectedItemId = R.id.navigation_profile
        loadProfileData()
    }

    private fun loadProfileData() {
        val prefs       = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        val email       = prefs.getString("user_email", "") ?: ""

        // Ambil data lengkap dari database
        val user = dbHelper.getUserByEmail(email)

        tvNama.text  = user?.nama  ?: prefs.getString("user_nama", "Pengguna") ?: "Pengguna"
        tvEmail.text = user?.email ?: email
        tvPhone.text = user?.phone ?: prefs.getString("user_phone", "-") ?: "-"
        tvProdi.text = if (user?.prodi?.isNotEmpty() == true) "${user.prodi} · ${user.universitas}" else "-"

        // Lokasi
        val lokasi = user?.lokasi ?: ""
        if (lokasi.isNotEmpty()) {
            tvLokasi.text = lokasi
            rowLokasi.visibility = View.VISIBLE
        } else {
            rowLokasi.visibility = View.GONE
        }

        // Bio
        val bio = user?.bio ?: ""
        if (bio.isNotEmpty()) {
            tvBio.text = bio
            rowBio.visibility = View.VISIBLE
        } else {
            rowBio.visibility = View.GONE
        }

        // LinkedIn
        val linkedin = user?.linkedin ?: ""
        if (linkedin.isNotEmpty()) {
            tvLinkedin.text = linkedin
            rowLinkedin.visibility = View.VISIBLE
        } else {
            rowLinkedin.visibility = View.GONE
        }

        // GitHub
        val github = user?.github ?: ""
        if (github.isNotEmpty()) {
            tvGithub.text = github
            rowGithub.visibility = View.VISIBLE
        } else {
            rowGithub.visibility = View.GONE
        }

        // Foto profil — gunakan AppUtils untuk konsistensi di semua layar
        AppUtils.loadProfilePhoto(this, ivProfileAvatar, dbHelper)
    }

    private fun openUrl(url: String) {
        val formattedUrl = if (!url.startsWith("http")) "https://$url" else url
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedUrl))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Tidak dapat membuka link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogoutConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Keluar")
            .setMessage("Apakah Anda yakin ingin keluar dari akun ini?")
            .setPositiveButton("Keluar") { _, _ ->
                val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("is_logged_in", false).apply()
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finishAffinity()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
