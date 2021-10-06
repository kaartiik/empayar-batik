package com.kaartiikvjn.empayarbatik.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.data.CartItem;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class BaseActivity extends AppCompatActivity {
    private Dialog dialog;
    private View dialogView;
    private SharedPreferences preferences;
    private FirebaseAuth auth;
    private CollectionReference usersReference;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);
        preferences = getSharedPreferences(Constants.prefName, Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        usersReference = FirebaseFirestore.getInstance().collection(Constants.users);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("ItemImages");
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogView = LayoutInflater.from(this).inflate(R.layout.layout_progress_dialog, null);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
    }

    public void showProgressDialog(String message) {
        TextView messageTextView = dialogView.findViewById(R.id.progress_dialog_message);
        messageTextView.setText(message);
        dialog.show();
    }

    public String getImageName() {
        return "image_" + new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(System.currentTimeMillis());
    }

    public void setDialogMessage(String message) {
        if (dialog.isShowing() && dialog != null) {
            TextView messageTextView = dialogView.findViewById(R.id.progress_dialog_message);
            messageTextView.setText(message);
        }
    }

    public void hideProgressDialog() {
        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }
    }

    public void savePreferenceString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getPreferenceString(String key) {
        return preferences.getString(key, "nullString");
    }

    public void savePreferenceFloat(String key, Float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    public Float getPreferenceFloat(String key) {
        return preferences.getFloat(key, 0f);
    }

    public void savePreferenceInt(String key, Integer value) {
        preferences.edit().putInt(key, value).apply();
    }

    public Integer getPreferenceInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void savePreferenceBoolean(String key, Boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public Boolean getPreferenceBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void longToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public FirebaseAuth getAuth() {
        return auth;
    }


    public void saveCartItem(ArrayList<CartItem> items) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = preferences.edit();
        String json = gson.toJson(items);
        editor.putString("items", json);
        editor.apply();
    }

    public ArrayList<CartItem> getCartItems() {
        ArrayList<CartItem> item;
        Gson gson = new Gson();
        String json = preferences.getString("items", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {
        }.getType();

        item = gson.fromJson(json, type);
        if (item == null) {
            return new ArrayList<>();
        } else
            return item;
    }

    public CollectionReference getUsersReference() {
        return usersReference;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}
