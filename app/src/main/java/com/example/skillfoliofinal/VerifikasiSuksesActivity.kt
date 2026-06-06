package com.example.skillfoliofinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class VerifikasiSuksesActivity : AppCompatActivity() {

    private lateinit var btnStatus: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi_sukses)

        btnStatus = findViewById(R.id.btn_status_verifikasi)

        val namaVerifikator = intent.getStringExtra("nama_verifikator") ?: "Drs. Joko Widodo"
        val emailVerifikator = intent.getStringExtra("email_verifikator") ?: "jokowi@instansi.go.id"

        btnStatus.setOnClickListener {
            AppUtils.navigateBack(this)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
