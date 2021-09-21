package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kaartiikvjn.empayarbatik.data.Item;
import com.kaartiikvjn.empayarbatik.databinding.FragmentCategoriesUniBinding;
import com.kaartiikvjn.empayarbatik.helper.ItemListAdapter;
import com.kaartiikvjn.empayarbatik.utils.BaseFragment;
import com.kaartiikvjn.empayarbatik.utils.Constants;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryFragment extends BaseFragment {
    private FragmentCategoriesUniBinding binding;
    private String category;
    private ArrayList<Item> items;
    private ItemListAdapter itemAdapter;
    private static final String TAG = "CategoryFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesUniBinding.inflate(inflater, container, false);
        Bundle bundle = this.getArguments();
        assert bundle != null;
        category = bundle.getString(Constants.itemCategory, "false");
        items = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemAdapter = new ItemListAdapter(items, this, requireContext());
        binding.categoryRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        binding.categoryRecyclerView.setAdapter(itemAdapter);
        showProgressDialog("Loading " + category + " from database");
        getDatabaseReference().child(Constants.items).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hideProgressDialog();
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (Objects.requireNonNull(item.child(Constants.itemCategory).getValue()).toString().equals(category)) {
                        items.add(new Item(
                                item.getKey(),
                                Objects.requireNonNull(item.child(Constants.itemTitle).getValue()).toString(),
                                Objects.requireNonNull(item.child(Constants.itemPhotoUrl).getValue()).toString(),
                                Objects.requireNonNull(item.child(Constants.itemCategory).getValue()).toString(),
                                Objects.requireNonNull(item.child(Constants.itemMaterial).getValue()).toString(),
                                Objects.requireNonNull(item.child(Constants.itemSpecialTraits).getValue()).toString(),
                                (ArrayList<String>) item.child(Constants.itemSize).getValue(),
                                Double.parseDouble(item.child(Constants.itemPrice).getValue().toString())
                        ));
                    }
                }
                if (items.isEmpty()) {
                    toast("No item found");
                }else {
                    itemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressDialog();
                toast("Error occurred : \n" + error.getMessage());

            }
        });
    }

    public void onTileTapped(int pos) {
        Intent intent = new Intent(requireActivity(), ItemDetails.class);
        intent.putExtra("id",items.get(pos).getItemId());
        startActivity(intent);
    }
}