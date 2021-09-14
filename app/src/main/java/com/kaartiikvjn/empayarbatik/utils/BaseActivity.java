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
import com.kaartiikvjn.empayarbatik.R;


public class BaseActivity extends AppCompatActivity {
    private Dialog dialog;
    private View dialogView;
    private SharedPreferences preferences;
    private FirebaseAuth auth;
    private CollectionReference usersReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);
        preferences = getSharedPreferences(Constants.prefName, Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        usersReference = FirebaseFirestore.getInstance().collection(Constants.users);
        databaseReference = FirebaseDatabase.getInstance().getReference();
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

    public CollectionReference getUsersReference() {
        return usersReference;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}