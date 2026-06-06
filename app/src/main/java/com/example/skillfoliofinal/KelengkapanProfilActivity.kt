package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator

class KelengkapanProfilActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var tvBatal: TextView
    private lateinit var pbProgress: CircularProgressIndicator
    private lateinit var tvProgressText: TextView
    private lateinit var tvProgressHint: TextView

    private lateinit var iconStep1: ImageView
    private lateinit var iconStep2: ImageView
    private lateinit var iconStep3: ImageView
    private lateinit var iconStep4: ImageView
    private lateinit var iconStep5: ImageView

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelengkapan_profil)

        dbHelper = DatabaseHelper(this)

        btnBack       = findViewById(R.id.btn_back)
        tvBatal       = findViewById(R.id.tv_batal)
        pbProgress    = findViewById(R.id.pb_kelengkapan)
        tvProgressText = findViewById(R.id.tv_progress_text)
        tvProgressHint = findViewById(R.id.tv_progress_hint)

        iconStep1 = findViewById(R.id.icon_step1)
        iconStep2 = findViewById(R.id.icon_step2)
        iconStep3 = findViewById(R.id.icon_step3)
        iconStep4 = findViewById(R.id.icon_step4)
        iconStep5 = findViewById(R.id.icon_step5)

        btnBack.setOnClickListener { AppUtils.navigateBack(this) }
        tvBatal.setOnClickListener { AppUtils.navigateBack(this) }

        // Step items bisa diklik untuk navigasi ke halaman terkait
        findViewById<android.view.View>(R.id.row_step2).setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, ListPengalamanActivity::class.java))
        }
        findViewById<android.view.View>(R.id.row_step3).setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, ListProjectActivity::class.java))
        }
        findViewById<android.view.View>(R.id.row_step5).setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, EditProfilActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        calculateProgress()
    }

    private fun calculateProgress() {
        val prefs     = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        val userEmail = prefs.getString("user_email", "") ?: ""

        // Ambil data user dari database
        val user = dbHelper.getUserByEmail(userEmail)

        val experiences = dbHelper.getAllPengalaman(userEmail)
        val projects    = dbHelper.getAllProject(userEmail)

        var completedSections = 0

        // ── Step 1: Data Diri Dasar (nama, email tidak kosong) ───────────────
        val step1Complete = (user?.nama?.isNotEmpty() == true) && userEmail.isNotEmpty()
        setStepIcon(iconStep1, step1Complete)
        if (step1Complete) completedSections++

        // ── Step 2: Riwayat Pengalaman ───────────────────────────────────────
        val step2Complete = experiences.isNotEmpty()
        setStepIcon(iconStep2, step2Complete)
        if (step2Complete) completedSections++

        // ── Step 3: Daftar Projects ──────────────────────────────────────────
        val step3Complete = projects.isNotEmpty()
        setStepIcon(iconStep3, step3Complete)
        if (step3Complete) completedSections++

        // ── Step 4: Keahlian & Kompetensi (punya pengalaman ATAU project) ────
        val step4Complete = step2Complete || step3Complete
        setStepIcon(iconStep4, step4Complete)
        if (step4Complete) completedSections++

        // ── Step 5: Kontak & Sosial Media ────────────────────────────────────
        // Lengkap jika: phone, lokasi, DAN (linkedin ATAU github) terisi
        val hasPhone    = user?.phone?.isNotEmpty() == true
        val hasLokasi   = user?.lokasi?.isNotEmpty() == true
        val hasSosmed   = (user?.linkedin?.isNotEmpty() == true) || (user?.github?.isNotEmpty() == true)
        val step5Complete = hasPhone && hasLokasi && hasSosmed
        setStepIcon(iconStep5, step5Complete)
        if (step5Complete) completedSections++

        // ── Hitung total persentase ──────────────────────────────────────────
        val totalPercentage = completedSections * 20
        pbProgress.progress = totalPercentage
        tvProgressText.text = "Profil $totalPercentage% Selesai"

        tvProgressHint.text = when {
            totalPercentage == 100 -> "🎉 Profil kamu sudah 100% lengkap!"
            totalPercentage >= 80  -> "Hampir selesai! Lengkapi kontak & sosial media."
            totalPercentage >= 60  -> "Bagus! Tambahkan proyek atau pengalaman kerja."
            totalPercentage >= 40  -> "Tambahkan pengalaman dan proyek untuk melengkapi."
            else                   -> "Mulai lengkapi profil untuk meningkatkan peluang karir."
        }
    }

    private fun setStepIcon(icon: ImageView, isComplete: Boolean) {
        icon.setImageResource(R.drawable.ic_check_circle)
        icon.imageTintList = if (isComplete) {
            ColorStateList.valueOf(Color.parseColor("#FFD600"))
        } else {
            ColorStateList.valueOf(Color.parseColor("#DDE3EC"))
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
