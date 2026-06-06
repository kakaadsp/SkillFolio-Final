package com.example.skillfoliofinal

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class TambahKeahlianActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var layoutSkillsList: LinearLayout
    private lateinit var btnSimpan: View
    private lateinit var btnBack: ImageButton
    private lateinit var btnCancelText: TextView

    private val allSkills = listOf(
        "Cloud Computing",
        "Cybersecurity",
        "Data Analysis",
        "Database Management",
        "Mobile Development",
        "Networking",
        "Programming",
        "UI/UX Design",
        "Web Development"
    )

    private val selectedSkills = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_keahlian)

        etSearch = findViewById(R.id.et_search)
        layoutSkillsList = findViewById(R.id.layout_skills_list)
        btnSimpan = findViewById(R.id.btn_simpan)
        btnBack = findViewById(R.id.btn_back)
        btnCancelText = findViewById(R.id.btn_cancel_text)

        // Set button text to make it correct
        if (btnSimpan is TextView) {
            (btnSimpan as TextView).text = "💾 Simpan Keahlian"
        }

        // Back behavior
        btnBack.setOnClickListener { AppUtils.navigateBack(this) }
        btnCancelText.setOnClickListener { AppUtils.navigateBack(this) }

        // Populate initially
        updateSkillsList("")

        // Search text listener
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSkillsList(s.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Save action
        btnSimpan.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putStringArrayListExtra("selected_skills", ArrayList(selectedSkills))
            setResult(Activity.RESULT_OK, resultIntent)
            AppUtils.navigateBack(this)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun updateSkillsList(query: String) {
        layoutSkillsList.removeAllViews()
        val filtered = allSkills.filter { it.contains(query, ignoreCase = true) }

        for (skill in filtered) {
            val tv = TextView(this)
            val density = resources.displayMetrics.density
            val heightPx = (48 * density).toInt()
            val marginPx = (8 * density).toInt()
            val paddingPx = (16 * density).toInt()

            tv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                heightPx
            ).apply {
                setMargins(0, 0, 0, marginPx)
            }
            tv.gravity = Gravity.CENTER_VERTICAL
            tv.setPadding(paddingPx, 0, paddingPx, 0)
            tv.text = skill
            tv.textSize = 14f

            tv.setOnClickListener {
                if (selectedSkills.contains(skill)) {
                    selectedSkills.remove(skill)
                    tv.setBackgroundResource(R.drawable.bg_card_gray)
                } else {
                    selectedSkills.add(skill)
                    tv.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_yellow))
                }
            }

            if (selectedSkills.contains(skill)) {
                tv.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_yellow))
            } else {
                tv.setBackgroundResource(R.drawable.bg_card_gray)
            }
            tv.setTextColor(Color.parseColor("#1A1A1A"))

            layoutSkillsList.addView(tv)
        }
    }
}
