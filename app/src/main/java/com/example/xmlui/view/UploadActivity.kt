package com.example.xmlui.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.xmlui.ImageAdapter
import com.example.xmlui.R
import com.example.xmlui.databinding.ActivityUploadBinding
import com.example.xmlui.viewModel.ImageViewModel
import java.io.ByteArrayOutputStream

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val viewModel: ImageViewModel by viewModels()
    private var imageUri: Uri? = null
    private lateinit var adapter: ImageAdapter

    private val PICK_IMAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ImageAdapter(emptyList())
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter

        binding.selectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }

        binding.uploadBtn.setOnClickListener {
            if (imageUri != null) {
                val bitmap = uriToBitmap(imageUri!!)
                val base64 = bitmapToBase64(bitmap)
                viewModel.uploadImage(base64)
            } else {
                Toast.makeText(this, "Pick an image first", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.images.observe(this) {
            adapter = ImageAdapter(it)
            binding.recyclerView.adapter = adapter
        }

        viewModel.loadImages()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            binding.selectImage.setImageURI(imageUri)
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return contentResolver.openInputStream(uri)?.use {
            android.graphics.BitmapFactory.decodeStream(it)
        } ?: throw Exception("Failed to decode bitmap")
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
        return Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)
    }
}