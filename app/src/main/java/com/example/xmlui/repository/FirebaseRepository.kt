package com.example.xmlui.repository

import com.example.xmlui.model.ImageData
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()
    private val imageCollection = db.collection("images")

    fun uploadImage(base64: String, onResult: (Boolean) -> Unit) {
        val map = hashMapOf("base64Image" to base64)
        imageCollection.add(map)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getAllImages(onData: (List<ImageData>) -> Unit) {
        imageCollection.get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull {
                    it.toObject(ImageData::class.java)
                }
                onData(list)
            }
    }
}