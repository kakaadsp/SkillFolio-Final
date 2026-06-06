package com.example.skillfoliofinal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TambahPengalamanActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvTitle: TextView
    private lateinit var etJudul: EditText
    private lateinit var etOrganisasi: EditText
    private lateinit var etPosisi: EditText
    private lateinit var etTahunMulai: EditText
    private lateinit var etTahunSelesai: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnBack: ImageButton
    private lateinit var btnCancelText: TextView
    private lateinit var cbMasihBekerja: CheckBox
    private lateinit var btnTambahKeahlian: TextView
    private lateinit var layoutSkillsContainer: LinearLayout
    private lateinit var btnDeleteText: TextView

    private var mode = "ADD"
    private var pengalamanId = 0
    private var userEmail = ""
    private val selectedSkillsList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pengalaman)

        dbHelper = DatabaseHelper(this)

        val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        userEmail = prefs.getString("user_email", "") ?: ""

        tvTitle = findViewById(R.id.tv_title)
        etJudul = findViewById(R.id.et_jabatan)
        etOrganisasi = findViewById(R.id.et_organisasi)
        etPosisi = findViewById(R.id.et_posisi)
        etTahunMulai = findViewById(R.id.et_tanggal_mulai)
        etTahunSelesai = findViewById(R.id.et_tanggal_selesai)
        etDeskripsi = findViewById(R.id.et_deskripsi)
        btnSimpan = findViewById(R.id.btn_simpan)
        btnBack = findViewById(R.id.btn_back)
        btnCancelText = findViewById(R.id.btn_cancel_text)
        cbMasihBekerja = findViewById(R.id.cb_masih_bekerja)
        btnTambahKeahlian = findViewById(R.id.btn_tambah_keahlian)
        layoutSkillsContainer = findViewById(R.id.layout_skills_container)
        btnDeleteText = findViewById(R.id.btn_delete_text)

        // Cancel / Back clicks
        btnBack.setOnClickListener { AppUtils.navigateBack(this) }
        btnCancelText.setOnClickListener { AppUtils.navigateBack(this) }

        // Checkbox logic
        cbMasihBekerja.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etTahunSelesai.setText("Present")
                etTahunSelesai.isEnabled = false
            } else {
                if (etTahunSelesai.text.toString() == "Present") {
                    etTahunSelesai.setText("")
                }
                etTahunSelesai.isEnabled = true
            }
        }

        // Skills logic
        btnTambahKeahlian.setOnClickListener {
            val intent = Intent(this, TambahKeahlianActivity::class.java)
            startActivityForResult(intent, 1002)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Setup date pickers (stubs or simple inputs, click opens dialog or we can let user input.
        // Let's make them show a mock dialog or simple editText dialog to input date easily)
        etTahunMulai.setOnClickListener {
            showMockDatePicker(etTahunMulai, "Pilih Tanggal Mulai")
        }
        etTahunSelesai.setOnClickListener {
            if (!cbMasihBekerja.isChecked) {
                showMockDatePicker(etTahunSelesai, "Pilih Tanggal Selesai")
            }
        }

        mode = intent.getStringExtra("mode") ?: "ADD"
        if (mode == "EDIT") {
            pengalamanId = intent.getIntExtra("pengalaman_id", 0)
            tvTitle.text = "Ubah Pengalaman"
            btnDeleteText.visibility = View.VISIBLE
            btnDeleteText.setOnClickListener {
                deleteData()
            }
            loadPengalamanData(pengalamanId)
        } else {
            tvTitle.text = "Tambah Pengalaman"
            btnDeleteText.visibility = View.GONE
        }

        btnSimpan.setOnClickListener {
            saveData()
        }
    }

    private fun showMockDatePicker(editText: EditText, title: String) {
        val options = arrayOf("Januari 2026", "Februari 2026", "Maret 2026", "April 2026", "Mei 2026", "Juni 2026", "Juli 2026", "Agustus 2026")
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(options) { _, which ->
                editText.setText(options[which])
            }
            .show()
    }

    private fun loadPengalamanData(id: Int) {
        val p = dbHelper.getPengalamanById(id)
        if (p != null) {
            etJudul.setText(p.judul)
            etOrganisasi.setText(p.organisasi)
            etPosisi.setText(p.posisi)
            etTahunMulai.setText(p.tahunMulai)
            etTahunSelesai.setText(p.tahunSelesai)
            etDeskripsi.setText(p.deskripsi)
            if (p.tahunSelesai.equals("Present", ignoreCase = true)) {
                cbMasihBekerja.isChecked = true
            }
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveData() {
        val judul = etJudul.text.toString().trim()
        val organisasi = etOrganisasi.text.toString().trim()
        val posisi = etPosisi.text.toString().trim()
        val tahunMulai = etTahunMulai.text.toString().trim()
        val tahunSelesai = etTahunSelesai.text.toString().trim()
        val deskripsi = etDeskripsi.text.toString().trim()

        if (judul.isEmpty() || organisasi.isEmpty() || posisi.isEmpty() ||
            tahunMulai.isEmpty() || tahunSelesai.isEmpty() || deskripsi.isEmpty()
        ) {
            Toast.makeText(this, "Field ini tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (mode == "ADD") {
            val p = Pengalaman(
                judul = judul,
                organisasi = organisasi,
                posisi = posisi,
                tahunMulai = tahunMulai,
                tahunSelesai = tahunSelesai,
                deskripsi = deskripsi,
                userEmail = userEmail
            )
            val result = dbHelper.insertPengalaman(p)
            if (result > 0) {
                Toast.makeText(this, "Berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                AppUtils.navigateBack(this)
            } else {
                Toast.makeText(this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show()
            }
        } else {
            val p = Pengalaman(
                id = pengalamanId,
                judul = judul,
                organisasi = organisasi,
                posisi = posisi,
                tahunMulai = tahunMulai,
                tahunSelesai = tahunSelesai,
                deskripsi = deskripsi,
                userEmail = userEmail
            )
            val result = dbHelper.updatePengalaman(p)
            if (result > 0) {
                Toast.makeText(this, "Berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                AppUtils.navigateBack(this)
            } else {
                Toast.makeText(this, "Gagal diperbarui", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteData() {
        if (pengalamanId != 0) {
            val result = dbHelper.deletePengalaman(pengalamanId)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002 && resultCode == Activity.RESULT_OK) {
            val skills = data?.getStringArrayListExtra("selected_skills") ?: return
            selectedSkillsList.addAll(skills)
            updateSkillsContainer()
        }
    }

    private fun updateSkillsContainer() {
        // Clear all except the first item (btn_tambah_keahlian)
        val childCount = layoutSkillsContainer.childCount
        if (childCount > 1) {
            layoutSkillsContainer.removeViews(1, childCount - 1)
        }

        val density = resources.displayMetrics.density
        val paddingPxHorizontal = (12 * density).toInt()
        val paddingPxVertical = (6 * density).toInt()
        val marginPx = (8 * density).toInt()

        for (skill in selectedSkillsList) {
            val tv = TextView(this).apply {
                text = "$skill ✕"
                textSize = 12f
                setTextColor(Color.parseColor("#1A1A1A"))
                setBackgroundResource(R.drawable.bg_tag_pill)
                setPadding(paddingPxHorizontal, paddingPxVertical, paddingPxHorizontal, paddingPxVertical)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, marginPx, 0)
                }
                setOnClickListener {
                    selectedSkillsList.remove(skill)
                    updateSkillsContainer()
                }
            }
            layoutSkillsContainer.addView(tv)
        }
    }
}
