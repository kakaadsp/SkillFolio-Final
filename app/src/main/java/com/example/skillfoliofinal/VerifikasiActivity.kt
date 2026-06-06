package com.example.skillfoliofinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerifikasiActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etJabatan: EditText
    private lateinit var etEmail: EditText
    private lateinit var etHubungan: EditText
    private lateinit var btnKirim: Button
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi)

        etNama = findViewById(R.id.et_nama_verifikator)
        etJabatan = findViewById(R.id.et_jabatan_verifikator)
        etEmail = findViewById(R.id.et_email_verifikator)
        etHubungan = findViewById(R.id.et_hubungan)
        btnKirim = findViewById(R.id.btn_kirim)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener { AppUtils.navigateBack(this) }

        btnKirim.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val jabatan = etJabatan.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val hubungan = etHubungan.text.toString().trim()

            if (nama.isEmpty() || jabatan.isEmpty() || email.isEmpty() || hubungan.isEmpty()) {
                Toast.makeText(this, "Field ini tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, VerifikasiSuksesActivity::class.java).apply {
                    putExtra("nama_verifikator", nama)
                    putExtra("email_verifikator", email)
                }
                AppUtils.navigateTo(this, intent, finishCurrent = true)
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
