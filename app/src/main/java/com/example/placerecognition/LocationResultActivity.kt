package com.example.placerecognition

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.gson.Gson
import com.bumptech.glide.Glide
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class LocationResultActivity : AppCompatActivity() {

    private lateinit var btnGallery: Button
    private lateinit var btnTakePicture: Button
    private lateinit var btnFindAnother: Button
    private lateinit var btnViewGallery: Button

    private lateinit var previewImage: ImageView
    private lateinit var placeNameTv: TextView
    private lateinit var latLonTv: TextView
    private lateinit var confidenceTv: TextView
    private lateinit var cityCountryTv: TextView
    private lateinit var processingTimeTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_result)

        // header buttons
        btnGallery      = findViewById(R.id.btnGallery)
        btnTakePicture  = findViewById(R.id.btnTakePicture)

        // content views
        previewImage      = findViewById(R.id.previewImage)
        placeNameTv       = findViewById(R.id.placeName)
        latLonTv          = findViewById(R.id.latLon)
        confidenceTv      = findViewById(R.id.confidence)
        cityCountryTv     = findViewById(R.id.cityCountry)
        processingTimeTv  = findViewById(R.id.processingTime)

        // footer buttons
        btnFindAnother  = findViewById(R.id.btnFindAnother)
        btnViewGallery  = findViewById(R.id.btnViewGallery)

        // 1) header nav
        btnGallery.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }
        btnTakePicture.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        // 2) show the passed image
        intent.getStringExtra(ImagePreviewActivity.EXTRA_IMAGE_URI)
            ?.let { Uri.parse(it) }
            ?.let { Glide.with(this).load(it).into(previewImage) }

        // 3) load & show dummy JSON
        loadDummyResult()?.let { displayResult(it) }

        // 4) footer actions
        btnFindAnother.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
            finish()
        }
        btnViewGallery.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
            finish()
        }
    }

    private fun loadDummyResult(): LocationResult? {
        return try {
            resources.openRawResource(R.raw.dummy_location).bufferedReader().use { reader ->
                Gson().fromJson(reader.readText(), LocationResult::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun displayResult(r: LocationResult) {
        placeNameTv.text      = r.placeName
        latLonTv.text         = "Lat/Lon: ${r.predictedLatitude}, ${r.predictedLongitude}"
        confidenceTv.text     = "Confidence: ${(r.confidenceScore * 100).toInt()}%"
        cityCountryTv.text    = "City/Country: ${r.city}, ${r.country}"
        processingTimeTv.text = "Processed in: ${"%.1f".format(r.processingTimeMs)} ms"
    }
}
