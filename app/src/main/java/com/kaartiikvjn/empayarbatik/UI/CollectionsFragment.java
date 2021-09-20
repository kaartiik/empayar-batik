package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kaartiikvjn.empayarbatik.UI.collection.CalmSea;
import com.kaartiikvjn.empayarbatik.UI.collection.Senada;
import com.kaartiikvjn.empayarbatik.UI.collection.Tropicana;
import com.kaartiikvjn.empayarbatik.databinding.FragmentCollectionsBinding;

public class CollectionsFragment extends Fragment {
    private FragmentCollectionsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCollectionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.calmSea.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), CalmSea.class));
        });
        binding.senada.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), Senada.class));
        });

        binding.tropicana.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), Tropicana.class));
        });
    }
}