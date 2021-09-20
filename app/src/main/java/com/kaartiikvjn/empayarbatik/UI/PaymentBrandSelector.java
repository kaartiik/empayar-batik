package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityPaymentBrandSelectorBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;

import java.util.Objects;

public class PaymentBrandSelector extends BaseActivity {
    private ActivityPaymentBrandSelectorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBrandSelectorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarPaymentBrandPage.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        binding.gridLayout.setOnClickListener(v -> {
            toast("Okay");
        });
        binding.proceed.setOnClickListener(v -> {
            startActivity(new Intent(PaymentBrandSelector.this, OrderCompleted.class));
            finishAffinity();
        });
    }

    private void toolbarSetter(ActionBar toolbar) {
        toolbar.setTitle("Pay Via");
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