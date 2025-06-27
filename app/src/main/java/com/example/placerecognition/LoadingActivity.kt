package com.example.placerecognition
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        // Simulate loading for 2 seconds (2000 ms)
        Handler(Looper.getMainLooper()).postDelayed({
            // After loading, start MainActivity
            startActivity(Intent(this, LocationResultActivity::class.java))
            finish()
        }, 2000)
    }
}

