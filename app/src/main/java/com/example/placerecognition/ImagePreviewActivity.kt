package com.example.placerecognition

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.view.View
class ImagePreviewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }

    private lateinit var previewImage: ImageView
    private lateinit var backButton: Button
    private lateinit var findLocationButton: Button
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        previewImage = findViewById(R.id.previewImage)
        backButton = findViewById(R.id.backButton)
        findLocationButton = findViewById(R.id.findLocationButton)

        // Get image URI from intent
        imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }

        imageUri?.let {
            Glide.with(this)
                .load(it)
                .into(previewImage)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }


        findLocationButton.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_URI, imageUri.toString())
            startActivity(intent)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }
}