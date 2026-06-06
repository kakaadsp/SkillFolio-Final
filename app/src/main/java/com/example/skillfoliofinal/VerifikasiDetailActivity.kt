package com.example.skillfoliofinal

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VerifikasiDetailActivity : AppCompatActivity() {

    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvTanggal: TextView
    private lateinit var tvCatatan: TextView
    private lateinit var btnSimpan: Button
    private lateinit var btnDeleteText: TextView
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi_detail)

        tvNama = findViewById(R.id.tv_verifikator_nama)
        tvEmail = findViewById(R.id.tv_verifikator_email)
        tvTanggal = findViewById(R.id.tv_verifikasi_tanggal)
        tvCatatan = findViewById(R.id.tv_verifikasi_catatan)
        btnSimpan = findViewById(R.id.btn_simpan)
        btnDeleteText = findViewById(R.id.btn_delete_text)
        btnBack = findViewById(R.id.btn_back)

        val nama = intent.getStringExtra("nama_verifikator") ?: "Drs. Joko Widodo"
        val email = intent.getStringExtra("email_verifikator") ?: "jokowi@instansi.go.id"

        tvNama.text = nama
        tvEmail.text = email

        btnBack.setOnClickListener { AppUtils.navigateBack(this) }
        btnSimpan.setOnClickListener { AppUtils.navigateBack(this) }
        btnDeleteText.setOnClickListener { AppUtils.navigateBack(this) }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
