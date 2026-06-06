package com.example.skillfoliofinal

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TotalPengalamanActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var rvTotal: RecyclerView
    private lateinit var btnClose: ImageButton
    private lateinit var tvBatal: TextView
    private lateinit var adapter: TotalPengalamanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_pengalaman)

        dbHelper = DatabaseHelper(this)
        rvTotal = findViewById(R.id.rv_total_pengalaman)
        btnClose = findViewById(R.id.btn_close)
        tvBatal = findViewById(R.id.tv_batal)

        btnClose.setOnClickListener { AppUtils.navigateBack(this) }
        tvBatal.setOnClickListener { AppUtils.navigateBack(this) }

        rvTotal.layoutManager = LinearLayoutManager(this)

        val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        val userEmail = prefs.getString("user_email", "") ?: ""

        val list = dbHelper.getAllPengalaman(userEmail)
        adapter = TotalPengalamanAdapter(list)
        rvTotal.adapter = adapter
    }

    class TotalPengalamanAdapter(private val list: List<Pengalaman>) :
        RecyclerView.Adapter<TotalPengalamanAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvOrganisasi: TextView = view.findViewById(R.id.tv_organisasi)
            val tvJudul: TextView = view.findViewById(R.id.tv_judul)
            val tvTahun: TextView = view.findViewById(R.id.tv_tahun)
            val tvStatus: TextView = view.findViewById(R.id.tv_status_badge)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pengalaman_total, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            holder.tvOrganisasi.text = item.organisasi
            holder.tvJudul.text = item.judul.uppercase()
            holder.tvTahun.text = "${item.tahunMulai} - ${item.tahunSelesai}"

            // Verification status simulation: alternating or checking item id
            if (item.id % 2 == 0) {
                holder.tvStatus.text = "Terverifikasi"
                holder.tvStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D4EDDA"))
                holder.tvStatus.setTextColor(Color.parseColor("#155724"))
            } else {
                holder.tvStatus.text = "Proses"
                holder.tvStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFF3CD"))
                holder.tvStatus.setTextColor(Color.parseColor("#856404"))
            }
        }

        override fun getItemCount(): Int = list.size
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
