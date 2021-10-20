package com.example.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.empayarbatik.databinding.ActivityOrderCompletedBinding;

public class OrderCompleted extends AppCompatActivity {
    private ActivityOrderCompletedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderCompletedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.textViewBack.setOnClickListener(v -> {
            startActivity(new Intent(OrderCompleted.this, MainActivity.class));
            finishAffinity();
        });
    }
}