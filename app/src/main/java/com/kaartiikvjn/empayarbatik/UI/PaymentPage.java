package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityPaymentPageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentPage extends AppCompatActivity {
    private ActivityPaymentPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarPaymentPage.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        setUpDropdown();
        binding.proceed.setOnClickListener(v -> {
            startActivity(new Intent(PaymentPage.this, PaymentBrandSelector.class));
        });
    }

    private void setUpDropdown() {
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            items.add("State " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PaymentPage.this, R.layout.list_item_layout, items);
        ((AutoCompleteTextView) Objects.requireNonNull(binding.dropdownField.getEditText())).setAdapter(adapter);
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