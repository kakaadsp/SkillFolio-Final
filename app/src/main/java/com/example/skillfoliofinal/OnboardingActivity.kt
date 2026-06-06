package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnboardingActivity : AppCompatActivity() {

    private var currentStep = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val ivIcon = findViewById<ImageView>(R.id.iv_onboarding_icon)
        val tvTitle = findViewById<TextView>(R.id.tv_onboarding_title)
        val tvDescription = findViewById<TextView>(R.id.tv_onboarding_description)
        val btnNext = findViewById<Button>(R.id.btn_next)

        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)

        btnNext.setOnClickListener {
            when (currentStep) {
                1 -> {
                    currentStep = 2
                    ivIcon.setImageResource(R.drawable.ic_shield)
                    tvTitle.text = "Validasi Otoritas"
                    tvDescription.text = "Kirimkan verifikasi langsung kepada dosen, atasan, atau verifikator eksternal untuk validitas data portofolio Anda."
                    
                    dot1.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DDE3EC"))
                    dot2.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFD600"))
                    dot3.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DDE3EC"))
                }
                2 -> {
                    currentStep = 3
                    ivIcon.setImageResource(R.drawable.ic_chart)
                    tvTitle.text = "Lacak Perkembangan"
                    tvDescription.text = "Lihat visualisasi tingkat kelengkapan portofolio dan kesesuaian skill Anda dengan standardisasi global."
                    
                    dot1.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DDE3EC"))
                    dot2.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DDE3EC"))
                    dot3.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFD600"))
                }
                3 -> {
                    // Set onboarding_done in SharedPreferences
                    val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("onboarding_done", true).apply()

                    startActivity(Intent(this, WelcomeActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                }
            }
        }
    }
}
