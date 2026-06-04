package com.example.attendease.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.attendease.MainActivity
import com.example.attendease.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logo)

        val animation =
            AnimationUtils.loadAnimation(
                this,
                R.anim.splash_fade_in
            )

        logo.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()

        }, 1500)
    }
}