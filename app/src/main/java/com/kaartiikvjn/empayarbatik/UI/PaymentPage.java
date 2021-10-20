package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.data.Customer;
import com.kaartiikvjn.empayarbatik.databinding.ActivityPaymentPageBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class PaymentPage extends BaseActivity {
    private ActivityPaymentPageBinding binding;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarPaymentPage.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        setUpDropdown();
        binding.proceed.setOnClickListener(v -> {
            if (isEverythingValid()) {
                customer = new Customer(
                        Objects.requireNonNull(binding.firstNameTextInputLayout.getEditText()).getText().toString() + Objects.requireNonNull(binding.lastNameTextInputLayout.getEditText()).getText().toString(),
                        "Malaysia",
                        Objects.requireNonNull(binding.streetAddressTextInputLayout.getEditText()).getText().toString(),
                        Objects.requireNonNull(binding.townCityTextInputLayout.getEditText()).getText().toString(),
                        Objects.requireNonNull(binding.dropdownField.getEditText()).getText().toString(),
                        Objects.requireNonNull(binding.zipCodeTextInputLayout.getEditText()).getText().toString(),
                        Objects.requireNonNull(binding.phoneNumberTextInputLayout.getEditText()).getText().toString()
                );
                makeOrder();
            }
        });
    }

    private Boolean isEverythingValid() {
        if (Objects.requireNonNull(binding.firstNameTextInputLayout.getEditText()).getText().toString().isEmpty()) {
            binding.firstNameTextInputLayout.getEditText().requestFocus();
            binding.firstNameTextInputLayout.getEditText().setError("Please enter first name");
            return false;
        } else if (Objects.requireNonNull(binding.lastNameTextInputLayout.getEditText()).getText().toString().isEmpty()) {
            binding.lastNameTextInputLayout.getEditText().requestFocus();
            binding.lastNameTextInputLayout.getEditText().setError("Please enter last name");
            return false;
        } else if (Objects.requireNonNull(binding.streetAddressTextInputLayout.getEditText()).getText().toString().isEmpty()) {
            binding.streetAddressTextInputLayout.getEditText().requestFocus();
            binding.streetAddressTextInputLayout.getEditText().setError("Please enter street address");
            return false;
        } else if (Objects.requireNonNull(binding.townCityTextInputLayout.getEditText()).getText().toString().isEmpty()) {
            binding.townCityTextInputLayout.getEditText().requestFocus();
            binding.townCityTextInputLayout.getEditText().setError("Please enter town/city");
            return false;
        } else if (Objects.requireNonNull(binding.zipCodeTextInputLayout.getEditText()).getText().toString().isEmpty()) {
            binding.zipCodeTextInputLayout.getEditText().requestFocus();
            binding.zipCodeTextInputLayout.getEditText().setError("Please enter zip code");
            return false;
        } else if (Objects.requireNonNull(binding.phoneNumberTextInputLayout.getEditText()).getText().toString().length()<10 || Objects.requireNonNull(binding.phoneNumberTextInputLayout.getEditText()).getText().toString().length()>=12 ){
            binding.phoneNumberTextInputLayout.getEditText().requestFocus();
            binding.phoneNumberTextInputLayout.getEditText().setError("Please enter a 10-12 digit valid phone number");
            return false;
        } else if (Objects.requireNonNull(binding.cardNumberTextInputLayout.getEditText()).getText().toString().length() != 16) {
            binding.cardNumberTextInputLayout.getEditText().requestFocus();
            binding.cardNumberTextInputLayout.getEditText().setError("Please enter a 16 digit valid card number");
            return false;
        }
        return true;
    }

    private void setUpDropdown() {
        List<String> items = new ArrayList<>();
        items.add("Johor");
        items.add("Kedah");
        items.add("Kelantan");
        items.add("Kuala Lumpur");
        items.add("Labuan");
        items.add("Melaka");
        items.add("Negeri Sembilan");
        items.add("Pahang");
        items.add("Penang");
        items.add("Perak");
        items.add("Perlis");
        items.add("Putrajaya");
        items.add("Sabah");
        items.add("Sarawak");
        items.add("Selangor");
        items.add("Terrenganu");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PaymentPage.this, R.layout.list_item_layout, items);
        ((AutoCompleteTextView) Objects.requireNonNull(binding.dropdownField.getEditText())).setAdapter(adapter);
    }

    private void makeOrder() {
        HashMap<String, Object> order = new HashMap<>();
        String key = getDatabaseReference().child(Constants.orders).push().getKey();
        order.put(Constants.orderPrice, getPreferenceFloat(Constants.orderPrice));
        order.put(Constants.orderCustomer, customer);
        order.put(Constants.orderItems, getCartItems());
        showProgressDialog("Creating order...");
        getDatabaseReference().child(Constants.orders).child(key).setValue(order)
                .addOnSuccessListener(command -> {
                    clearShoppingCart();
                }).addOnFailureListener(command -> {
            hideProgressDialog();
            toast("Failed to create order");
        });
    }

    private void clearShoppingCart() {
        getDatabaseReference().child(Constants.shoppingCarts).child(Objects.requireNonNull(getAuth().getUid())).removeValue()
                .addOnSuccessListener(unused -> {
                    hideProgressDialog();
                    toast("Order created successfully");
                    startActivity(new Intent(PaymentPage.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    hideProgressDialog();
                });
    }

    private void toolbarSetter(ActionBar toolbar) {
        toolbar.setTitle("Complete Purchase");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);
        toolbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}