package com.example.skillfoliofinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView

class ProjectAdapter(
    private var list: List<Project>,
    private val onItemClick: (Int) -> Unit,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaProject: TextView = view.findViewById(R.id.tv_nama_project)
        val tvTeknologi: TextView = view.findViewById(R.id.tv_teknologi)
        val tvTahun: TextView = view.findViewById(R.id.tv_tahun)
        val tvDeskripsi: TextView = view.findViewById(R.id.tv_deskripsi)
        val btnMenu: ImageButton = view.findViewById(R.id.btn_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNamaProject.text = item.namaProject
        holder.tvTeknologi.text = item.teknologi
        holder.tvTahun.text = item.tahun
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

    fun updateList(newList: List<Project>) {
        this.list = newList
        notifyDataSetChanged()
    }
}

