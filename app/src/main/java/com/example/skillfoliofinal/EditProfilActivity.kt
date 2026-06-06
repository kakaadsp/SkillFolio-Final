package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class EditProfilActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    private lateinit var ivAvatar: ImageView
    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etProdi: EditText
    private lateinit var etUniversitas: EditText
    private lateinit var etBio: EditText
    private lateinit var etPassword: EditText
    private lateinit var etLokasi: EditText
    private lateinit var etLinkedin: EditText
    private lateinit var etGithub: EditText

    private var currentEmail = ""
    private var currentUser: User? = null
    private var selectedPhotoUri: Uri? = null

    // ── Modern Activity Result API untuk image picker ──────────────────────
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri
            ivAvatar.setImageURI(uri)
            ivAvatar.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        dbHelper = DatabaseHelper(this)

        // Bind views
        ivAvatar      = findViewById(R.id.iv_avatar)
        etNama        = findViewById(R.id.et_nama)
        etEmail       = findViewById(R.id.et_email)
        etPhone       = findViewById(R.id.et_phone)
        etProdi       = findViewById(R.id.et_prodi)
        etUniversitas = findViewById(R.id.et_universitas)
        etBio         = findViewById(R.id.et_bio)
        etPassword    = findViewById(R.id.et_password)
        etLokasi      = findViewById(R.id.et_lokasi)
        etLinkedin    = findViewById(R.id.et_linkedin)
        etGithub      = findViewById(R.id.et_github)

        val btnBatal  = findViewById<Button>(R.id.btn_batal)
        val btnSimpan = findViewById<Button>(R.id.btn_simpan)
        val cvAvatar  = findViewById<CardView>(R.id.cv_avatar_wrapper)

        // ── Load current user data ──────────────────────────────────────────
        val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        currentEmail = prefs.getString("user_email", "") ?: ""
        currentUser  = dbHelper.getUserByEmail(currentEmail)

        etEmail.setText(currentEmail)
        etEmail.isEnabled = false

        currentUser?.let { u ->
            etNama.setText(u.nama)
            etPhone.setText(u.phone)
            etProdi.setText(u.prodi)
            etUniversitas.setText(u.universitas)
            etBio.setText(u.bio)
            etPassword.setText(u.password)
            etLokasi.setText(u.lokasi)
            etLinkedin.setText(u.linkedin)
            etGithub.setText(u.github)

            // Load saved photo if exists — gunakan AppUtils untuk konsistensi
            if (u.fotoUri.isNotEmpty()) {
                try {
                    val uri = Uri.parse(u.fotoUri)
                    ivAvatar.setImageURI(uri)
                    ivAvatar.scaleType = ImageView.ScaleType.CENTER_CROP
                    selectedPhotoUri = uri
                } catch (e: Exception) {
                    ivAvatar.setImageResource(R.drawable.ic_avatar)
                }
            } else {
                AppUtils.loadProfilePhoto(this, ivAvatar, dbHelper)
            }
        }

        // ── Photo picker — tap avatar card ──────────────────────────────────
        cvAvatar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnBatal.setOnClickListener {
            AppUtils.navigateBack(this)
        }

        btnSimpan.setOnClickListener { saveProfile(prefs) }
    }

    private fun saveProfile(prefs: android.content.SharedPreferences) {
        val nama        = etNama.text.toString().trim()
        val phone       = etPhone.text.toString().trim()
        val prodi       = etProdi.text.toString().trim()
        val universitas = etUniversitas.text.toString().trim()
        val bio         = etBio.text.toString().trim()
        val password    = etPassword.text.toString().trim()
        val lokasi      = etLokasi.text.toString().trim()
        val linkedin    = etLinkedin.text.toString().trim()
        val github      = etGithub.text.toString().trim()

        // ── Validasi field wajib ────────────────────────────────────────────
        if (nama.isEmpty() || phone.isEmpty() || prodi.isEmpty() ||
            universitas.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nama, No HP, Prodi, Universitas, dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            etPassword.error = "Password minimal 6 karakter"
            etPassword.requestFocus()
            return
        }

        // ── Simpan URI foto (persistent permission) ─────────────────────────
        val fotoUriStr = if (selectedPhotoUri != null) {
            try {
                // Take persistable URI permission agar bisa dibuka setelah restart
                contentResolver.takePersistableUriPermission(
                    selectedPhotoUri!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                // Beberapa URI tidak mendukung persistent permission, lanjutkan saja
            }
            selectedPhotoUri.toString()
        } else {
            currentUser?.fotoUri ?: ""
        }

        // ── Update ke SQLite ────────────────────────────────────────────────
        val updatedUser = User(
            nama        = nama,
            email       = currentEmail,
            phone       = phone,
            prodi       = prodi,
            universitas = universitas,
            password    = password,
            bio         = bio,
            lokasi      = lokasi,
            linkedin    = linkedin,
            github      = github,
            fotoUri     = fotoUriStr
        )
        val result = dbHelper.updateUser(updatedUser)

        // ── Sinkron ke SharedPreferences ───────────────────────────────────
        prefs.edit().apply {
            putString("user_nama",        nama)
            putString("user_phone",       phone)
            putString("user_prodi",       prodi)
            putString("user_universitas", universitas)
            putString("user_foto_uri",    fotoUriStr)
            apply()
        }

        if (result > 0) {
            Toast.makeText(this, "Profil berhasil diperbarui! ✓", Toast.LENGTH_SHORT).show()
            AppUtils.navigateBack(this)
        } else {
            Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
