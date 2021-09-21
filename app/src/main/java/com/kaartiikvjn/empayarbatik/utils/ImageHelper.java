package com.kaartiikvjn.empayarbatik.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaartiikvjn.empayarbatik.R;

public class ImageHelper {
    private Context context;
    private StorageReference bucketReference;
    private static final String TAG = "ImageHelper";

    public ImageHelper(Context context) {
        this.context = context;
        bucketReference = FirebaseStorage.getInstance().getReference();
    }

    public void loadCloudImage(String url, ImageView target) {
        bucketReference.child(url).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context).load(uri)
                    .placeholder(R.drawable.photo_loading)
                    .error(R.drawable.photo_unavailable)
                    .into(target);
        }).addOnFailureListener(e -> Log.d(TAG, "loadCloudImage: " + e.getMessage()));
    }
}
