package com.kaartiikvjn.empayarbatik.UI.collection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivitySenadaBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;

public class Senada extends BaseActivity {
    private ActivitySenadaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySenadaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.whatsAppButton.setOnClickListener(v -> toast("Clicked on it."));
    }
}