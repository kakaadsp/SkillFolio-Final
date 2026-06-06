package com.example.skillfoliofinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView

class PengalamanAdapter(
    private var list: List<Pengalaman>,
    private val onItemClick: (Int) -> Unit,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<PengalamanAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudul: TextView = view.findViewById(R.id.tv_judul)
        val tvOrganisasi: TextView = view.findViewById(R.id.tv_organisasi)
        val tvTahun: TextView = view.findViewById(R.id.tv_tahun)
        val tvDeskripsi: TextView = view.findViewById(R.id.tv_deskripsi)
        val btnMenu: ImageButton = view.findViewById(R.id.btn_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pengalaman, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvJudul.text = item.judul
        holder.tvOrganisasi.text = item.organisasi
        holder.tvTahun.text = "${item.tahunMulai} - ${item.tahunSelesai}"
        holder.tvDeskripsi.text = item.deskripsi

        holder.itemView.setOnClickListener { onItemClick(item.id) }

        holder.btnMenu.setOnClickListener { view ->
            val context = view.context
            val popup = PopupMenu(context, view)
            popup.menu.add("Edit")
            popup.menu.add("Hapus")
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                    "Edit" -> {
                        onEdit(item.id)
                        true
                    }
                    "Hapus" -> {
                        onDelete(item.id)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<Pengalaman>) {
        this.list = newList
        notifyDataSetChanged()
    }
}

