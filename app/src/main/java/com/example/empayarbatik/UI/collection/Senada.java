package com.example.empayarbatik.UI.collection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.empayarbatik.databinding.ActivitySenadaBinding;
import com.example.empayarbatik.utils.BaseActivity;

public class Senada extends BaseActivity {
    private ActivitySenadaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySenadaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.whatsAppButton.setOnClickListener(v ->{
            String url = "https://wa.link/oyr9xz";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }
}