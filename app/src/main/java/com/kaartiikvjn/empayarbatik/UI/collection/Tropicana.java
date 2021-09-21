package com.kaartiikvjn.empayarbatik.UI.collection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityTropicanaBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;

public class Tropicana extends BaseActivity {
    private ActivityTropicanaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTropicanaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.whatsAppButton.setOnClickListener(v -> toast("Whatsapp tapped"));
    }
}