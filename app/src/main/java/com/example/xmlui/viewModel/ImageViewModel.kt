package com.example.xmlui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xmlui.model.ImageData
import com.example.xmlui.repository.FirebaseRepository

class ImageViewModel: ViewModel() {
    private val repository = FirebaseRepository()

    private val _images = MutableLiveData<List<ImageData>>()
    val images: LiveData<List<ImageData>> = _images

    fun uploadImage(base64: String) {
        repository.uploadImage(base64) {
            if (it) loadImages()
        }
    }

    fun loadImages() {
        repository.getAllImages {
            _images.value = it
        }
    }
}