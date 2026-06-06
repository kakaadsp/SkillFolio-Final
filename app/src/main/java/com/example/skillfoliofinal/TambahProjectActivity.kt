package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TambahProjectActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvTitle: TextView
    private lateinit var etNama: EditText
    private lateinit var etTeknologi: EditText
    private lateinit var etTahun: EditText
    private lateinit var etLink: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnBack: ImageButton
    private lateinit var btnCancelText: TextView
    private lateinit var btnDeleteText: TextView

    private var mode = "ADD"
    private var projectId = 0
    private var userEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_project)

        dbHelper = DatabaseHelper(this)

        val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        userEmail = prefs.getString("user_email", "") ?: ""

        tvTitle = findViewById(R.id.tv_title)
        etNama = findViewById(R.id.et_nama)
        etTeknologi = findViewById(R.id.et_teknologi)
        etTahun = findViewById(R.id.et_tahun)
        etLink = findViewById(R.id.et_link)
        etDeskripsi = findViewById(R.id.et_deskripsi)
        btnSimpan = findViewById(R.id.btn_simpan)
        btnBack = findViewById(R.id.btn_back)
        btnCancelText = findViewById(R.id.btn_cancel_text)
        btnDeleteText = findViewById(R.id.btn_delete_text)

        btnBack.setOnClickListener { AppUtils.navigateBack(this) }
        btnCancelText.setOnClickListener { AppUtils.navigateBack(this) }

        mode = intent.getStringExtra("mode") ?: "ADD"
        if (mode == "EDIT") {
            projectId = intent.getIntExtra("project_id", 0)
            tvTitle.text = "Ubah Project"
            btnDeleteText.visibility = View.VISIBLE
            btnDeleteText.setOnClickListener {
                deleteData()
            }
            loadProjectData(projectId)
        } else {
            tvTitle.text = "Tambah Project"
            btnDeleteText.visibility = View.GONE
        }

        btnSimpan.setOnClickListener {
            saveData()
        }
    }

    private fun loadProjectData(id: Int) {
        val p = dbHelper.getProjectById(id)
        if (p != null) {
            etNama.setText(p.namaProject)
            etTeknologi.setText(p.teknologi)
            etTahun.setText(p.tahun)
            etLink.setText(p.link)
            etDeskripsi.setText(p.deskripsi)
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveData() {
        val nama = etNama.text.toString().trim()
        val teknologi = etTeknologi.text.toString().trim()
        val tahun = etTahun.text.toString().trim()
        val link = etLink.text.toString().trim()
        val deskripsi = etDeskripsi.text.toString().trim()

        if (nama.isEmpty() || teknologi.isEmpty() || tahun.isEmpty() ||
            link.isEmpty() || deskripsi.isEmpty()
        ) {
            Toast.makeText(this, "Field ini tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (mode == "ADD") {
            val p = Project(
                namaProject = nama,
                teknologi = teknologi,
                tahun = tahun,
                link = link,
                deskripsi = deskripsi,
                userEmail = userEmail
            )
            val result = dbHelper.insertProject(p)
            if (result > 0) {
                Toast.makeText(this, "Berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                AppUtils.navigateBack(this)
            } else {
                Toast.makeText(this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show()
            }
        } else {
            val p = Project(
                id = projectId,
                namaProject = nama,
                teknologi = teknologi,
                tahun = tahun,
                link = link,
                deskripsi = deskripsi,
                userEmail = userEmail
            )
            val result = dbHelper.updateProject(p)
            if (result > 0) {
                Toast.makeText(this, "Berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                AppUtils.navigateBack(this)
            } else {
                Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteData() {
        if (projectId != 0) {
            val result = dbHelper.deleteProject(projectId)
            if (result > 0) {
                Toast.makeText(this, "Berhasil dihapus!", Toast.LENGTH_SHORT).show()
                AppUtils.navigateBack(this)
            } else {
                Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
