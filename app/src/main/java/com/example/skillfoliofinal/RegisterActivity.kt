package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        val etNama        = findViewById<EditText>(R.id.et_nama)
        val etEmail       = findViewById<EditText>(R.id.et_email)
        val etPhone       = findViewById<EditText>(R.id.et_phone)
        val etProdi       = findViewById<EditText>(R.id.et_prodi)
        val etUniversitas = findViewById<EditText>(R.id.et_universitas)
        val etPassword    = findViewById<EditText>(R.id.et_password)
        val btnDaftar     = findViewById<Button>(R.id.btn_daftar)

        btnDaftar.setOnClickListener {
            val nama        = etNama.text.toString().trim()
            val email       = etEmail.text.toString().trim()
            val phone       = etPhone.text.toString().trim()
            val prodi       = etProdi.text.toString().trim()
            val universitas = etUniversitas.text.toString().trim()
            val password    = etPassword.text.toString().trim()

            // ── Validasi kelengkapan field ─────────────────────────────────────
            if (nama.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                prodi.isEmpty() || universitas.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ── Validasi format email ──────────────────────────────────────────
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Format email tidak valid"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // ── Validasi panjang password ──────────────────────────────────────
            if (password.length < 6) {
                etPassword.error = "Password minimal 6 karakter"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // ── Cek apakah email sudah terdaftar ──────────────────────────────
            if (dbHelper.isEmailTaken(email)) {
                etEmail.error = "Email ini sudah terdaftar"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // ── Simpan ke SQLite ───────────────────────────────────────────────
            val result = dbHelper.registerUser(
                nama        = nama,
                email       = email,
                phone       = phone,
                prodi       = prodi,
                universitas = universitas,
                password    = password
            )

            if (result > 0) {
                // Simpan session minimal (email saja) ke SharedPreferences
                // Data lengkap selalu dibaca dari database
                getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putString("user_email", email.lowercase().trim())
                    .apply()

                Toast.makeText(this, "Registrasi berhasil! Silakan masuk.", Toast.LENGTH_SHORT).show()
                AppUtils.navigateTo(this, Intent(this, LoginActivity::class.java), finishCurrent = true)
            } else {
                Toast.makeText(this, "Registrasi gagal. Coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

