package com.example.skillfoliofinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailPengalamanActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvJudul: TextView
    private lateinit var tvOrganisasi: TextView
    private lateinit var tvTahun: TextView
    private lateinit var tvPosisi: TextView
    private lateinit var tvDeskripsi: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var btnVerifikasi: Button
    private lateinit var btnBatal: Button
    private lateinit var btnSimpan: Button
    private lateinit var ivAvatar: android.widget.ImageView

    private var pengalamanId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pengalaman)

        dbHelper = DatabaseHelper(this)
        pengalamanId = intent.getIntExtra("pengalaman_id", -1)

        tvJudul = findViewById(R.id.tv_judul)
        tvOrganisasi = findViewById(R.id.tv_organisasi)
        tvTahun = findViewById(R.id.tv_tahun)
        tvPosisi = findViewById(R.id.tv_posisi)
        tvDeskripsi = findViewById(R.id.tv_deskripsi)

        btnEdit = findViewById(R.id.btn_edit)
        btnDelete = findViewById(R.id.btn_delete)
        btnVerifikasi = findViewById(R.id.btn_verifikasi)
        btnBatal = findViewById(R.id.btn_batal)
        btnSimpan = findViewById(R.id.btn_simpan)
        ivAvatar = findViewById(R.id.iv_avatar)

        ivAvatar.setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, ProfileActivity::class.java))
        }
        btnBatal.setOnClickListener { AppUtils.navigateBack(this) }
        btnSimpan.setOnClickListener { AppUtils.navigateBack(this) }

        btnEdit.setOnClickListener {
            val intent = Intent(this, TambahPengalamanActivity::class.java).apply {
                putExtra("mode", "EDIT")
                putExtra("pengalaman_id", pengalamanId)
            }
            AppUtils.navigateTo(this, intent)
        }

        btnDelete.setOnClickListener {
            if (pengalamanId != -1) {
                val result = dbHelper.deletePengalaman(pengalamanId)
                if (result > 0) {
                    Toast.makeText(this, "Berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    AppUtils.navigateBack(this)
                } else {
                    Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnVerifikasi.setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, VerifikasiActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload foto profil
        AppUtils.loadProfilePhoto(this, ivAvatar, dbHelper)
        loadData()
    }

    private fun loadData() {
        if (pengalamanId != -1) {
            val p = dbHelper.getPengalamanById(pengalamanId)
            if (p != null) {
                tvJudul.text = p.judul
                tvOrganisasi.text = p.organisasi
                tvTahun.text = "${p.tahunMulai} - ${p.tahunSelesai}"
                tvPosisi.text = p.posisi
                tvDeskripsi.text = p.deskripsi
            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
