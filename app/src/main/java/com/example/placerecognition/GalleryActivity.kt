package com.example.placerecognition

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.placerecognition.ImagePreviewActivity

class GalleryActivity : AppCompatActivity() {

    private lateinit var folderSpinner: Spinner
    private lateinit var galleryRecyclerView: RecyclerView
    private lateinit var confirmButton: Button
    private lateinit var adapter: GalleryAdapter

    private val imageMap = mutableMapOf<String, MutableList<Uri>>()
    private var selectedImageUri: Uri? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) loadImages()
        else Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        folderSpinner = findViewById(R.id.folderSpinner)
        galleryRecyclerView = findViewById(R.id.galleryRecyclerView)
        confirmButton = findViewById(R.id.confirmButton)

        adapter = GalleryAdapter(emptyList()) { uri ->
            selectedImageUri = uri
            adapter.setSelected(uri)
            confirmButton.visibility = Button.VISIBLE
        }

        galleryRecyclerView.layoutManager = GridLayoutManager(this, 3)
        galleryRecyclerView.adapter = adapter

        confirmButton.setOnClickListener {
            selectedImageUri?.let {
                val intent = Intent(this, ImagePreviewActivity::class.java)
                intent.putExtra(ImagePreviewActivity.EXTRA_IMAGE_URI, it.toString())
                startActivity(intent)
            }
        }

        checkPermissionAndLoadImages()
    }

    private fun checkPermissionAndLoadImages() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(permission)
        } else {
            loadImages()
        }
    }

    private fun loadImages() {
        imageMap.clear()
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        val cursor = contentResolver.query(
            uriExternal,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val folderColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val folderName = it.getString(folderColumn) ?: "Unknown"
                val contentUri = Uri.withAppendedPath(uriExternal, id.toString())

                if (!imageMap.containsKey(folderName)) {
                    imageMap[folderName] = mutableListOf()
                }
                imageMap[folderName]?.add(contentUri)
            }
        }

        setupSpinner()
    }

    private fun setupSpinner() {
        val folderList = imageMap.keys.toList()
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, folderList)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        folderSpinner.adapter = adapterSpinner
        folderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val folder = folderList[position]
                val images = imageMap[folder] ?: emptyList()
                selectedImageUri = null
                confirmButton.visibility = Button.GONE
                adapter.updateImages(images)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Default selection
        if (folderList.isNotEmpty()) {
            folderSpinner.setSelection(0)
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