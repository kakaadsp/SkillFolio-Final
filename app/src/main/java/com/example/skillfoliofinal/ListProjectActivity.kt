package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListProjectActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ProjectAdapter
    private lateinit var rvProject: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var etSearch: EditText
    private lateinit var tvCountBadge: TextView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var ivAvatar: ImageView

    private var userEmail: String = ""
    private var listData: List<Project> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_project)

        dbHelper = DatabaseHelper(this)

        val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
        userEmail = prefs.getString("user_email", "") ?: ""

        rvProject = findViewById(R.id.rv_project)
        tvEmpty = findViewById(R.id.tv_empty)
        etSearch = findViewById(R.id.et_search)
        tvCountBadge = findViewById(R.id.tv_project_count_badge)
        bottomNav = findViewById(R.id.bottom_nav)
        ivAvatar = findViewById(R.id.iv_avatar)

        // Load foto profil di avatar top-bar
        AppUtils.loadProfilePhoto(this, ivAvatar, dbHelper)

        ivAvatar.setOnClickListener {
            AppUtils.navigateTo(this, Intent(this, ProfileActivity::class.java))
        }

        val fabAdd = findViewById<View>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val intent = Intent(this, TambahProjectActivity::class.java)
            intent.putExtra("mode", "ADD")
            AppUtils.navigateTo(this, intent)
        }

        rvProject.layoutManager = LinearLayoutManager(this)
        adapter = ProjectAdapter(
            emptyList(),
            onItemClick = { id ->
                val intent = Intent(this, DetailProjectActivity::class.java)
                intent.putExtra("project_id", id)
                AppUtils.navigateTo(this, intent)
            },
            onEdit = { id ->
                val intent = Intent(this, TambahProjectActivity::class.java)
                intent.putExtra("mode", "EDIT")
                intent.putExtra("project_id", id)
                AppUtils.navigateTo(this, intent)
            },
            onDelete = { id ->
                showDeleteDialog(id)
            }
        )
        rvProject.adapter = adapter

        // Setup Search
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Setup bottom nav selection & listener
        bottomNav.selectedItemId = R.id.navigation_projects
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    AppUtils.navigateTab(this, Intent(this, MainActivity::class.java))
                    false
                }
                R.id.navigation_experience -> {
                    AppUtils.navigateTab(this, Intent(this, ListPengalamanActivity::class.java))
                    false
                }
                R.id.navigation_projects -> true
                R.id.navigation_profile -> {
                    AppUtils.navigateTab(this, Intent(this, ProfileActivity::class.java))
                    false
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNav.selectedItemId = R.id.navigation_projects
        // Reload foto profil setiap kali kembali ke halaman ini
        AppUtils.loadProfilePhoto(this, ivAvatar, dbHelper)
        loadData()
    }

    private fun loadData() {
        if (userEmail.isNotEmpty()) {
            listData = dbHelper.getAllProject(userEmail)
            adapter.updateList(listData)
            tvCountBadge.text = "${listData.size} Projects"

            if (listData.isEmpty()) {
                tvEmpty.visibility = View.VISIBLE
                rvProject.visibility = View.GONE
            } else {
                tvEmpty.visibility = View.GONE
                rvProject.visibility = View.VISIBLE
            }
        }
    }

    private fun filterList(query: String) {
        val filtered = listData.filter {
            it.namaProject.contains(query, ignoreCase = true) ||
            it.teknologi.contains(query, ignoreCase = true) ||
            it.deskripsi.contains(query, ignoreCase = true)
        }
        adapter.updateList(filtered)
        if (filtered.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvProject.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvProject.visibility = View.VISIBLE
        }
    }

    private fun showDeleteDialog(id: Int) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Project")
            .setMessage("Apakah Anda yakin ingin menghapus project ini?")
            .setPositiveButton("Hapus") { _, _ ->
                val result = dbHelper.deleteProject(id)
                if (result > 0) {
                    Toast.makeText(this, "Berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    loadData()
                } else {
                    Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
