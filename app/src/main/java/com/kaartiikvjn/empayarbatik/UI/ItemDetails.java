package com.kaartiikvjn.empayarbatik.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityItemDetailsBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;
import com.kaartiikvjn.empayarbatik.utils.ImageHelper;

import java.util.ArrayList;
import java.util.Objects;

public class ItemDetails extends BaseActivity {
    private ActivityItemDetailsBinding binding;
    private ImageHelper imageHelper;
    private ArrayList<String> sizes;
    private static final String TAG = "ItemDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageHelper = new ImageHelper(this);
        sizes = new ArrayList<>();
        showProgressDialog("Loading item details");
        getDatabaseReference().child(Constants.items).child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hideProgressDialog();
                binding.mainLayout.setVisibility(View.VISIBLE);
                imageHelper.loadCloudImage(Objects.requireNonNull(snapshot.child(Constants.itemPhotoUrl).getValue()).toString(), binding.itemDetailsPhoto);
                binding.itemDetailsTitle.setText(Objects.requireNonNull(snapshot.child(Constants.itemTitle).getValue()).toString());
                binding.itemDetailsPrice.setText("RM " + String.format("%.2f", Double.parseDouble(Objects.requireNonNull(snapshot.child(Constants.itemPrice).getValue()).toString())));
                binding.itemDetailsMaterial.setText(Objects.requireNonNull(snapshot.child(Constants.itemMaterial).getValue()).toString());
                binding.itemDetailsSpecialTraits.setText(Objects.requireNonNull(snapshot.child(Constants.itemSpecialTraits).getValue()).toString());
                sizes = (ArrayList<String>) snapshot.child(Constants.itemSize).getValue();
                setUpSizeSpinner(sizes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressDialog();
                toast("Error occurred : \n" + error.getMessage());

            }
        });

        binding.itemDetailsQuantityAdd.setOnClickListener(v -> {
            int quantity = Integer.parseInt(binding.itemDetailsQuantity.getText().toString());
            quantity = quantity + 1;
            binding.itemDetailsQuantity.setText(String.valueOf(quantity));
        });
        binding.itemDetailsQuantityRemove.setOnClickListener(v -> {
            int quantity = Integer.parseInt(binding.itemDetailsQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                binding.itemDetailsQuantity.setText(String.valueOf(quantity));
            } else {
                toast("Quantity can't be 0");
            }
        });
        binding.itemDetailsAddToCart.setOnClickListener(v -> {
            toast("Item added to cart");
        });
    }

    private void setUpSizeSpinner(ArrayList<String> sizes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_layout, sizes);
        ((AutoCompleteTextView) binding.sizeInputLayout.getEditText()).setAdapter(adapter);
        binding.sizeInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.itemDetailsAddToCart.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}