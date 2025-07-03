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

        // ✅ Get the image URI passed in
        val imageUri = intent.getStringExtra(ImagePreviewActivity.EXTRA_IMAGE_URI)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LocationResultActivity::class.java)

            // ✅ Pass it forward using the same key!
            intent.putExtra(ImagePreviewActivity.EXTRA_IMAGE_URI, imageUri)

            startActivity(intent)
            finish()
        }, 2000)
    }
}

