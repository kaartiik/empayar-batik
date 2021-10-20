package com.example.empayarbatik.UI;

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
import com.example.empayarbatik.R;
import com.example.empayarbatik.data.Item;
import com.example.empayarbatik.databinding.ActivityItemDetailsBinding;
import com.example.empayarbatik.utils.BaseActivity;
import com.example.empayarbatik.utils.Constants;
import com.example.empayarbatik.utils.ImageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ItemDetails extends BaseActivity {
    private ActivityItemDetailsBinding binding;
    private ImageHelper imageHelper;
    private ArrayList<String> sizes;
    private Item item;
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
                item = new Item(
                        snapshot.getKey(),
                        Objects.requireNonNull(snapshot.child(Constants.itemTitle).getValue()).toString(),
                        Objects.requireNonNull(snapshot.child(Constants.itemPhotoUrl).getValue()).toString(),
                        Objects.requireNonNull(snapshot.child(Constants.itemCategory).getValue()).toString(),
                        Objects.requireNonNull(snapshot.child(Constants.itemMaterial).getValue()).toString(),
                        Objects.requireNonNull(snapshot.child(Constants.itemSpecialTraits).getValue()).toString(),
                        (ArrayList<String>) snapshot.child(Constants.itemSize).getValue(),
                        Double.parseDouble(snapshot.child(Constants.itemPrice).getValue().toString())
                );
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
        binding.itemDetailsAddToCart.setOnClickListener(v -> addToCart());
    }

    private void addToCart() {
        HashMap<String, Object> cartItem = new HashMap<>();
        cartItem.put(Constants.cartItemId, item.getItemId());
        cartItem.put(Constants.cartItemTitle, item.getItemTitle());
        cartItem.put(Constants.cartItemPrice, item.getItemPrice());
        cartItem.put(Constants.cartItemSize, Objects.requireNonNull(binding.sizeInputLayout.getEditText()).getText().toString());
        cartItem.put(Constants.cartItemQuantity, binding.itemDetailsQuantity.getText());
        String key = getDatabaseReference().child(Constants.shoppingCarts).child(Objects.requireNonNull(getAuth().getUid()))
                .push().getKey();
        showProgressDialog("Adding item to cart");
        assert key != null;
        getDatabaseReference().child(Constants.shoppingCarts).child(Objects.requireNonNull(getAuth().getUid()))
                .child(key).setValue(cartItem).addOnSuccessListener(unused -> {
            hideProgressDialog();
            toast("Item added to cart successfully");
        }).addOnFailureListener(e -> {
            hideProgressDialog();
            toast("Failed to add item to cart");
        });
    }

    private void setUpSizeSpinner(ArrayList<String> sizes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_layout, sizes);
        ((AutoCompleteTextView) Objects.requireNonNull(binding.sizeInputLayout.getEditText())).setAdapter(adapter);
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