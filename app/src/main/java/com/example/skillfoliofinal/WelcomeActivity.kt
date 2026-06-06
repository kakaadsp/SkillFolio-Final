package com.example.skillfoliofinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val btnDaftar = findViewById<Button>(R.id.btn_daftar)
        val btnMasuk = findViewById<Button>(R.id.btn_masuk)

        btnDaftar.setOnClickListener {
            // Pop-in: layar Register "pop in" dari tengah
            AppUtils.navigateTo(this, Intent(this, RegisterActivity::class.java))
        }

        btnMasuk.setOnClickListener {
            // Pop-in: layar Login "pop in" dari tengah
            AppUtils.navigateTo(this, Intent(this, LoginActivity::class.java))
        }
    }

    override fun finish() {
        super.finish()
        // Dismiss: fade + scale out
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
