package com.kaartiikvjn.empayarbatik.UI;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kaartiikvjn.empayarbatik.data.Item;
import com.kaartiikvjn.empayarbatik.databinding.ActivitySearchResultBinding;
import com.kaartiikvjn.empayarbatik.helper.ItemListAdapter;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;

import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")
public class SearchResultActivity extends BaseActivity {
    private ActivitySearchResultBinding binding;
    private ArrayList<Item> items;
    private ArrayList<Item> tempItems;
    private ItemListAdapter itemAdapter;
    private String query = "";
    private static final String TAG = "ActivitySearchResult.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleIntent(getIntent());
        items = new ArrayList<>();
        tempItems = new ArrayList<>();
        itemAdapter = new ItemListAdapter(tempItems, this);
        binding.searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.searchRecyclerView.setAdapter(itemAdapter);
        itemAdapter.setOnItemTappedListener(position -> {
            Intent intent = new Intent(SearchResultActivity.this, ItemDetails.class);
            intent.putExtra("id", tempItems.get(position).getItemId());
            startActivity(intent);
        });
        showProgressDialog("Loading data from database");
        getDatabaseReference().child(Constants.items).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hideProgressDialog();
                for (DataSnapshot item : snapshot.getChildren()) {
                    items.add(new Item(
                            item.getKey(),
                            Objects.requireNonNull(item.child(Constants.itemTitle).getValue()).toString(),
                            Objects.requireNonNull(item.child(Constants.itemPhotoUrl).getValue()).toString(),
                            Objects.requireNonNull(item.child(Constants.itemCategory).getValue()).toString(),
                            Objects.requireNonNull(item.child(Constants.itemMaterial).getValue()).toString(),
                            Objects.requireNonNull(item.child(Constants.itemSpecialTraits).getValue()).toString(),
                            (ArrayList<String>) item.child(Constants.itemSize).getValue(),
                            Double.parseDouble(Objects.requireNonNull(item.child(Constants.itemPrice).getValue()).toString())
                    ));
                    if (Objects.requireNonNull(item.child(Constants.itemTitle).getValue()).toString().toLowerCase().contains(query.toLowerCase())) {
                        tempItems.add(new Item(
                                item.getKey(),
                                Objects.requireNonNull(item.child(Constants.itemTitle).getValue()).toString(),
                                Objects.requireNonNull(item.child(Constants.itemPhotoUrl).getValue()).toString(),
                                Objects.requireNonNull(item.child(Constants.itemCategory).getValue()).toString(),
                                Objects.requireNonNull(item.child(Constants.itemMaterial).getValue()).toString(),
                                Objects.requireNonNull(item.child(Constants.itemSpecialTraits).getValue()).toString(),
                                (ArrayList<String>) item.child(Constants.itemSize).getValue(),
                                Double.parseDouble(Objects.requireNonNull(item.child(Constants.itemPrice).getValue()).toString())
                        ));
                    }
                }
                if (tempItems.isEmpty()) {
                    binding.noItemMatchedYourSearch.setText(String.format("No item matched \"%s\"", query));
                } else {
                    itemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressDialog();
                toast("Error occurred : \n" + error.getMessage());
            }
        });
        binding.searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    tempItems.clear();
                    tempItems.addAll(items);
                    itemAdapter.notifyDataSetChanged();
                } else {
                    sortList(query = s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sortList(String s) {
        tempItems.clear();
        for (Item item : items) {
            if (item.getItemTitle().toLowerCase().contains(query.toLowerCase())) {
                tempItems.add(item);
            }
        }
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            binding.searchEditText.setText(query);
            binding.searchEditText.setSelection(query.length());
        }
    }
}