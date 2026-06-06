package com.example.skillfoliofinal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CvGeneratorActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_generator)

        bottomNav = findViewById(R.id.bottom_nav)
        val ivAvatar = findViewById<ImageView>(R.id.iv_avatar)

        ivAvatar.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Setup bottom nav selection & listener
        bottomNav.selectedItemId = R.id.navigation_profile
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    false
                }
                R.id.navigation_experience -> {
                    startActivity(Intent(this, ListPengalamanActivity::class.java))
                    finish()
                    false
                }
                R.id.navigation_projects -> {
                    startActivity(Intent(this, ListProjectActivity::class.java))
                    finish()
                    false
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    false
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNav.selectedItemId = R.id.navigation_profile
    }
}
