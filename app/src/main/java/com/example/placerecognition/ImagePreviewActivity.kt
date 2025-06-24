package com.example.placerecognition

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ImagePreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val imageView: ImageView = findViewById(R.id.previewImageView)
        val imageUri = intent.getStringExtra("image_uri")

        if (imageUri != null) {
            imageView.setImageURI(Uri.parse(imageUri))
        }

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish() // return to CameraActivity
        }

        findViewById<Button>(R.id.findLocationButton).setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("image_uri", imageUri)
            startActivity(intent)
        }
    }
}