package com.example.skillfoliofinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("skillfolio_prefs", Context.MODE_PRIVATE)
            val onboardingDone = prefs.getBoolean("onboarding_done", false)
            val isLoggedIn = prefs.getBoolean("is_logged_in", false)

            val intent = if (!onboardingDone) {
                Intent(this, OnboardingActivity::class.java)
            } else if (isLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, WelcomeActivity::class.java)
            }
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, 2000)
    }
}
