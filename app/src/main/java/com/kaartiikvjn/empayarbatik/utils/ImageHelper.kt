package com.kaartiikvjn.empayarbatik.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kaartiikvjn.empayarbatik.R

class ImageHelper(private val context: Context) {
    companion object {
        private const val TAG = "ImageHelper"
    }

    var bucketReference: StorageReference? = null


    fun loadImage(url: String?, imageView: ImageView?) {
        bucketReference!!.child(url!!).downloadUrl.addOnSuccessListener { uri: Uri? ->
            Glide.with(
                context
            )
                .load(uri).
                placeholder(R.drawable.photo_loading)
                .error(R.drawable.photo_unavailable)
                .into(imageView!!)
        }.addOnFailureListener { e: Exception ->
            Log.d(
                TAG,
                "loadImage: " + e.message + "\n path:$url"
            )
        }
    }

    init {
        bucketReference = FirebaseStorage.getInstance().reference
    }
}