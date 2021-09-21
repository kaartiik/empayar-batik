package com.kaartiikvjn.empayarbatik.UI.collection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityCalmSeaBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;

public class CalmSea extends BaseActivity {
    private ActivityCalmSeaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalmSeaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.whatsAppButton.setOnClickListener(v -> toast("Clicked on it."));
    }
}