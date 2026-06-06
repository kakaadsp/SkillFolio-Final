package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvGreeting: TextView
    private lateinit var tvTotalPengalaman: TextView
    private lateinit var tvTotalProjects: TextView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var ivAvatar: ImageView

    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check login status
        val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)
        if (!isLoggedIn) {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        userEmail = prefs.getString("user_email", "") ?: ""
        val userNama = prefs.getString("user_nama", "Pengguna") ?: "Pengguna"

        tvGreeting = findViewById(R.id.tv_greeting)
        tvTotalPengalaman = findViewById(R.id.tv_total_pengalaman)
        tvTotalProjects = findViewById(R.id.tv_total_projects)
        bottomNav = findViewById(R.id.bottom_nav)
        ivAvatar = findViewById(R.id.iv_avatar)

        tvGreeting.text = "Selamat datang, $userNama."

        // Load foto profil di avatar top-bar
        AppUtils.loadProfilePhoto(this, ivAvatar, dbHelper)

        // Navigate to Profile when clicking the top-bar avatar
        ivAvatar.setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, ProfileActivity::class.java))
        }

        // TOTAL PENGALAMAN card click listener
        findViewById<View>(R.id.card_total_pengalaman).setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, TotalPengalamanActivity::class.java))
        }

        // TOTAL PROJECTS card click listener
        findViewById<View>(R.id.card_total_projects).setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, ListProjectActivity::class.java))
        }

        // Kelengkapan Profil card click listener
        findViewById<View>(R.id.card_kelengkapan_profil).setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, KelengkapanProfilActivity::class.java))
        }

        // Quick add buttons
        findViewById<View>(R.id.btn_tambah_pengalaman).setOnClickListener {
            val intent = Intent(this, TambahPengalamanActivity::class.java)
            intent.putExtra("mode", "ADD")
            AppUtils.navigateTo(this, intent)
        }

        findViewById<View>(R.id.btn_tambah_project).setOnClickListener {
            val intent = Intent(this, TambahProjectActivity::class.java)
            intent.putExtra("mode", "ADD")
            AppUtils.navigateTo(this, intent)
        }

        // Bottom Navigation behavior
        bottomNav.selectedItemId = R.id.navigation_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_experience -> {
                    AppUtils.navigateTab(this, Intent(this, ListPengalamanActivity::class.java))
                    false
                }
                R.id.navigation_projects -> {
                    AppUtils.navigateTab(this, Intent(this, ListProjectActivity::class.java))
                    false
                }
                R.id.navigation_profile -> {
                    AppUtils.navigateTab(this, Intent(this, ProfileActivity::class.java))
                    false
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Ensure bottom_nav selection is correct when returning
        bottomNav.selectedItemId = R.id.navigation_home
        // Reload foto profil setiap kali kembali ke halaman ini
        AppUtils.loadProfilePhoto(this, ivAvatar, dbHelper)
        loadStats()
    }

    private fun loadStats() {
        if (userEmail.isNotEmpty()) {
            val pengalamanList = dbHelper.getAllPengalaman(userEmail)
            val projectList = dbHelper.getAllProject(userEmail)

            tvTotalPengalaman.text = pengalamanList.size.toString()
            tvTotalProjects.text = projectList.size.toString()
        }
    }
}