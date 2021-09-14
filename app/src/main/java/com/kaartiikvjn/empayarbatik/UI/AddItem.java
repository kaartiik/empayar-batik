package com.kaartiikvjn.empayarbatik.UI;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityAddItemBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;

import java.util.Objects;

public class AddItem extends BaseActivity {
    private ActivityAddItemBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.addItemToolbar.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imageUri = Objects.requireNonNull(result.getData()).getData();
                        Glide.with(this).load(imageUri).into(binding.addItemImageView);
                    }
                });
        binding.addItemImagePicker.setOnClickListener(v -> {
            pickImage();
        });
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(intent);
    }

    private void toolbarSetter(ActionBar actionBar) {
        actionBar.setTitle("Add Item");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_item_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout!")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        getAuth().signOut();
                        dialog.dismiss();
                        startActivity(new Intent(AddItem.this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item_menu, menu);
        return true;
    }
}