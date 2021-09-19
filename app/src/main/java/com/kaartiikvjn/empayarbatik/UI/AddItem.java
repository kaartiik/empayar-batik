package com.kaartiikvjn.empayarbatik.UI;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.UploadTask;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityAddItemBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddItem extends BaseActivity {
    private ActivityAddItemBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri imageUri;
    private ArrayList<String> sizes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.addItemToolbar.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        sizes = new ArrayList<>();
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
        binding.xsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sizes.add(getString(R.string.xs));
            } else {
                sizes.remove(getString(R.string.xs));
            }
        });
        binding.sCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sizes.add(getString(R.string.s));
            } else {
                sizes.remove(getString(R.string.s));
            }
        });
        binding.mCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sizes.add(getString(R.string.m));
            } else {
                sizes.remove(getString(R.string.m));
            }
        });
        binding.lCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sizes.add(getString(R.string.l));
            } else {
                sizes.remove(getString(R.string.l));
            }
        });
        binding.xlCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sizes.add(getString(R.string.xl));
            } else {
                sizes.remove(getString(R.string.xl));
            }
        });
        binding.xllCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sizes.add(getString(R.string.xll));
            } else {
                sizes.remove(getString(R.string.xll));
            }
        });
        spinnerSetter();
        binding.buttonAddItem.setOnClickListener(v -> {
            showProgressDialog("Adding Item.......");
            if (imageUri == null) {
                toast("Please select an item image");
            } else if (binding.addItemTitle.getText().toString().isEmpty()) {
                binding.addItemTitle.requestFocus();
                binding.addItemTitle.setError("Item title is required.");
            } else if (binding.addItemPrice.getText().toString().isEmpty()) {
                binding.addItemPrice.requestFocus();
                binding.addItemPrice.setError("Item Price is required.");
            } else if (binding.addItemTraits.getText().toString().isEmpty()) {
                binding.addItemTraits.requestFocus();
                binding.addItemTraits.setError("At least one special trait is required.");
            } else if (binding.addItemMaterial.getText().toString().isEmpty()) {
                binding.addItemMaterial.requestFocus();
                binding.addItemMaterial.setError("Please specify the item material");
            } else {
                String itemKey = getDatabaseReference().child(Constants.items).push().getKey();
                Bitmap bitmap = null;
                ContentResolver contentResolver = getContentResolver();
                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, imageUri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                assert (bitmap != null);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                String imageName = getImageName();
                UploadTask uploadTask =
                        getStorageReference().child(imageName)
                                .putBytes(data);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    HashMap<String, Object> itemData = new HashMap<>();
                    itemData.put(Constants.itemPhotoUrl, "ItemImages/" + imageName);
                    itemData.put(Constants.itemTitle, binding.addItemTitle.getText().toString());
                    itemData.put(Constants.itemPrice, binding.addItemPrice.getText().toString());
                    itemData.put(Constants.itemSize, sizes);
                    itemData.put(Constants.itemSpecialTraits, binding.addItemTraits.getText().toString());
                    itemData.put(Constants.itemMaterial, binding.addItemMaterial.getText().toString());
                    itemData.put(Constants.itemCategory, binding.categorySpinner.getText().toString());
                    getDatabaseReference().child(Constants.items).child(itemKey).setValue(itemData).addOnSuccessListener(unused -> {
                        hideProgressDialog();
                        toast("Item successfully added");
                        clearForm();
                    });
                });
            }
        });
    }

    private void clearForm() {
        binding.addItemImageView.setImageResource(android.R.color.transparent);
        binding.addItemTitle.setText("");
        binding.addItemPrice.setText("");
        binding.categorySpinner.setSelectedIndex(0);
        binding.addItemTraits.setText("");
        binding.addItemMaterial.setText("");
        binding.xsCheckbox.setChecked(false);
        binding.sCheckbox.setChecked(false);
        binding.mCheckbox.setChecked(false);
        binding.lCheckbox.setChecked(false);
        binding.xlCheckbox.setChecked(false);
        binding.xllCheckbox.setChecked(false);
    }

    private void spinnerSetter() {
        binding.categorySpinner.setItems(Constants.newArrivals, Constants.top, Constants.dress, Constants.pants);
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
        } else if (item.getItemId() == R.id.add_item_clearForm) {
            clearForm();
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