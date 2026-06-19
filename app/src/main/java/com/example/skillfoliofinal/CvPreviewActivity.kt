package com.example.skillfoliofinal

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CvPreviewActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    // Profile views
    private lateinit var ivCvPhoto: ImageView
    private lateinit var tvCvNama: TextView
    private lateinit var tvCvProdi: TextView
    private lateinit var tvCvUniversitas: TextView
    private lateinit var tvCvEmail: TextView
    private lateinit var tvCvPhone: TextView
    private lateinit var tvCvLokasi: TextView
    private lateinit var tvCvLinkedin: TextView
    private lateinit var tvCvBio: TextView
    private lateinit var sectionBio: LinearLayout

    // Pengalaman
    private lateinit var containerPengalaman: LinearLayout
    private lateinit var tvEmptyPengalaman: TextView

    // Project
    private lateinit var containerProject: LinearLayout
    private lateinit var tvEmptyProject: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_preview)

        dbHelper = DatabaseHelper(this)

        // Bind views
        ivCvPhoto         = findViewById(R.id.iv_cv_photo)
        tvCvNama          = findViewById(R.id.tv_cv_nama)
        tvCvProdi         = findViewById(R.id.tv_cv_prodi)
        tvCvUniversitas   = findViewById(R.id.tv_cv_universitas)
        tvCvEmail         = findViewById(R.id.tv_cv_email)
        tvCvPhone         = findViewById(R.id.tv_cv_phone)
        tvCvLokasi        = findViewById(R.id.tv_cv_lokasi)
        tvCvLinkedin      = findViewById(R.id.tv_cv_linkedin)
        tvCvBio           = findViewById(R.id.tv_cv_bio)
        sectionBio        = findViewById(R.id.section_bio)

        containerPengalaman  = findViewById(R.id.container_pengalaman)
        tvEmptyPengalaman    = findViewById(R.id.tv_empty_pengalaman)

        containerProject  = findViewById(R.id.container_project)
        tvEmptyProject    = findViewById(R.id.tv_empty_project)

        // Back button
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            AppUtils.navigateBack(this)
        }

        loadAllData()
    }

    private fun loadAllData() {
        val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        val email = prefs.getString("user_email", "") ?: ""

        val user = dbHelper.getUserByEmail(email)

        // ─── Profile ────────────────────────────────────────────────────────
        tvCvNama.text         = user?.nama ?: prefs.getString("user_nama", "Pengguna") ?: "Pengguna"
        tvCvProdi.text        = if (user?.prodi?.isNotEmpty() == true) user.prodi else ""
        tvCvUniversitas.text  = if (user?.universitas?.isNotEmpty() == true) user.universitas else ""
        tvCvEmail.text        = user?.email ?: email
        tvCvPhone.text        = user?.phone?.takeIf { it.isNotEmpty() } ?: "-"
        tvCvLokasi.text       = user?.lokasi?.takeIf { it.isNotEmpty() } ?: "-"
        tvCvLinkedin.text     = user?.linkedin?.takeIf { it.isNotEmpty() } ?: "-"

        val bio = user?.bio ?: ""
        if (bio.isNotEmpty()) {
            tvCvBio.text = bio
            sectionBio.visibility = View.VISIBLE
        } else {
            sectionBio.visibility = View.GONE
        }

        // Load foto profil
        val fotoUri = user?.fotoUri ?: ""
        if (fotoUri.isNotEmpty()) {
            try {
                ivCvPhoto.setImageURI(Uri.parse(fotoUri))
                ivCvPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
            } catch (e: Exception) {
                ivCvPhoto.setImageResource(R.drawable.ic_avatar)
            }
        } else {
            ivCvPhoto.setImageResource(R.drawable.ic_avatar)
        }

        // ─── Pengalaman ──────────────────────────────────────────────────────
        val pengalamanList = dbHelper.getAllPengalaman(email)
        if (pengalamanList.isEmpty()) {
            tvEmptyPengalaman.visibility = View.VISIBLE
        } else {
            tvEmptyPengalaman.visibility = View.GONE
            pengalamanList.forEachIndexed { index, item ->
                val isLast = index == pengalamanList.lastIndex
                val itemView = buildPengalamanItem(item, isLast)
                containerPengalaman.addView(itemView)
            }
        }

        // ─── Project ─────────────────────────────────────────────────────────
        val projectList = dbHelper.getAllProject(email)
        if (projectList.isEmpty()) {
            tvEmptyProject.visibility = View.VISIBLE
        } else {
            tvEmptyProject.visibility = View.GONE
            projectList.forEach { item ->
                val itemView = buildProjectItem(item)
                containerProject.addView(itemView)
            }
        }
    }

    /**
     * Membangun tampilan item pengalaman secara programatik.
     * Menggunakan timeline-style dengan garis vertikal di sebelah kiri.
     */
    private fun buildPengalamanItem(item: Pengalaman, isLast: Boolean): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.bottomMargin = if (isLast) 0 else dp(16) }
        }

        // Timeline indicator: dot + line
        val timelineLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER_HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(dp(20), LinearLayout.LayoutParams.MATCH_PARENT)
        }

        val dot = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(dp(10), dp(10)).also {
                it.topMargin = dp(4)
            }
            background = getDrawable(R.drawable.bg_avatar_circle)
            backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#063669")
            )
        }

        val line = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(dp(2), LinearLayout.LayoutParams.MATCH_PARENT).also {
                it.topMargin = dp(2)
                it.gravity = android.view.Gravity.CENTER_HORIZONTAL
            }
            setBackgroundColor(if (isLast) android.graphics.Color.TRANSPARENT else android.graphics.Color.parseColor("#DDE3EC"))
        }

        timelineLayout.addView(dot)
        timelineLayout.addView(line)

        // Content card
        val contentLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also {
                it.marginStart = dp(12)
                it.bottomMargin = dp(8)
            }
        }

        // Judul + periode on same row
        val headerRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER_VERTICAL
        }

        val tvJudul = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            text = item.judul
            textSize = 13f
            setTextColor(android.graphics.Color.parseColor("#1A1A1A"))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val periode = "${item.tahunMulai} – ${item.tahunSelesai}"
        val tvPeriode = TextView(this).apply {
            text = periode
            textSize = 10f
            setTextColor(android.graphics.Color.parseColor("#888888"))
        }

        headerRow.addView(tvJudul)
        headerRow.addView(tvPeriode)

        val tvOrganisasi = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.topMargin = dp(2) }
            text = if (item.posisi.isNotEmpty()) "${item.posisi} · ${item.organisasi}"
                   else item.organisasi
            textSize = 11f
            setTextColor(android.graphics.Color.parseColor("#705D00"))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val tvDeskripsi = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.topMargin = dp(4) }
            text = item.deskripsi
            textSize = 11f
            setTextColor(android.graphics.Color.parseColor("#555555"))
            setLineSpacing(dp(2).toFloat(), 1f)
        }

        contentLayout.addView(headerRow)
        contentLayout.addView(tvOrganisasi)
        if (item.deskripsi.isNotEmpty()) contentLayout.addView(tvDeskripsi)

        root.addView(timelineLayout)
        root.addView(contentLayout)

        return root
    }

    /**
     * Membangun tampilan item project secara programatik.
     * Menggunakan card-style dengan nama, teknologi, dan deskripsi.
     */
    private fun buildProjectItem(item: Project): View {
        val cardContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.bottomMargin = dp(12) }
            setBackgroundColor(android.graphics.Color.parseColor("#F5F8FF"))
            setPadding(dp(14), dp(12), dp(14), dp(12))
        }

        // Top row: name + tech tags
        val topRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER_VERTICAL
        }

        val tvNamaProject = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            text = item.namaProject
            textSize = 13f
            setTextColor(android.graphics.Color.parseColor("#063669"))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val tvTahun = TextView(this).apply {
            text = item.tahun
            textSize = 10f
            setTextColor(android.graphics.Color.parseColor("#888888"))
        }

        topRow.addView(tvNamaProject)
        topRow.addView(tvTahun)

        // Tech tags
        val techRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.topMargin = dp(4) }
        }

        val techList = item.teknologi.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        techList.forEach { tech ->
            val tag = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { it.marginEnd = dp(6) }
                text = tech
                textSize = 9f
                setTextColor(android.graphics.Color.parseColor("#274E82"))
                setBackgroundColor(android.graphics.Color.parseColor("#D5E3FF"))
                setPadding(dp(6), dp(2), dp(6), dp(2))
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            techRow.addView(tag)
        }

        val tvDeskripsi = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.topMargin = dp(6) }
            text = item.deskripsi
            textSize = 11f
            setTextColor(android.graphics.Color.parseColor("#555555"))
            setLineSpacing(dp(2).toFloat(), 1f)
        }

        cardContainer.addView(topRow)
        if (techList.isNotEmpty()) cardContainer.addView(techRow)
        if (item.deskripsi.isNotEmpty()) cardContainer.addView(tvDeskripsi)

        // Link row
        if (item.link.isNotEmpty()) {
            val tvLink = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { it.topMargin = dp(4) }
                text = "🔗 ${item.link}"
                textSize = 10f
                setTextColor(android.graphics.Color.parseColor("#0A66C2"))
            }
            cardContainer.addView(tvLink)
        }

        return cardContainer
    }

    /** Converts dp to pixels */
    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
