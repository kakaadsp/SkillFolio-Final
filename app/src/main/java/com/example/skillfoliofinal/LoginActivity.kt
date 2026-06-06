package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val etEmail   = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnMasuk  = findViewById<Button>(R.id.btn_masuk)
        val tvDaftar  = findViewById<TextView>(R.id.tv_daftar)

        // Bold the "Daftar" word in the bottom helper text
        tvDaftar.text = Html.fromHtml("Belum punya akun? <b>Daftar</b>", Html.FROM_HTML_MODE_LEGACY)

        btnMasuk.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // ── Validasi field kosong ──────────────────────────────────────────
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ── Validasi format email ──────────────────────────────────────────
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Format email tidak valid"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // ── Verifikasi ke SQLite ───────────────────────────────────────────
            val user = dbHelper.loginUser(email, password)
            if (user != null) {
                // Login sukses → simpan sesi & data profil ke SharedPreferences
                getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE).edit().apply {
                    putBoolean("is_logged_in",     true)
                    putString("user_email",         user.email)
                    putString("user_nama",          user.nama)
                    putString("user_phone",         user.phone)
                    putString("user_prodi",         user.prodi)
                    putString("user_universitas",   user.universitas)
                    // Sengaja TIDAK menyimpan password di SharedPreferences
                    apply()
                }

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            } else {
                // Berikan pesan spesifik sesuai kondisi
                if (!dbHelper.isEmailTaken(email)) {
                    etEmail.error = "Email belum terdaftar"
                    etEmail.requestFocus()
                } else {
                    etPassword.error = "Password salah"
                    etPassword.requestFocus()
                }
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
            }
        }

        tvDaftar.setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, RegisterActivity::class.java))
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

