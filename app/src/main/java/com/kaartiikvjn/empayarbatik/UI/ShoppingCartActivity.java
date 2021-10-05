package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.data.CartItem;
import com.kaartiikvjn.empayarbatik.databinding.ActivityShoppingCartBinding;
import com.kaartiikvjn.empayarbatik.helper.CartItemAdapter;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;

import java.util.ArrayList;
import java.util.Objects;

public class ShoppingCartActivity extends BaseActivity {
    private ActivityShoppingCartBinding binding;
    private ArrayList<CartItem> cartItems;
    private ArrayList<String> keys;
    private CartItemAdapter cartItemAdapter;
    private ChildEventListener mListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarShoppingCart.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        cartItems = new ArrayList<>();
        recyclerViewSetter();
        binding.proceed.setOnClickListener(v -> {
            startActivity(new Intent(ShoppingCartActivity.this, PaymentPage.class));
        });
        mListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        getDatabaseReference().child("");
    }

    private void toolbarSetter(ActionBar toolbar) {
        toolbar.setTitle("Shopping Cart");
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

    private void recyclerViewSetter() {
        cartItemAdapter = new CartItemAdapter(cartItems, ShoppingCartActivity.this);
        binding.shoppingCartRecyclerView.setLayoutManager(new LinearLayoutManager(ShoppingCartActivity.this));
        binding.shoppingCartRecyclerView.setAdapter(cartItemAdapter);
    }

    public void onRemoveItemTapped(int position) {

    }

    public void onDecQuantityTapped(int position) {

    }

    public void onIncreaseQuantityTapped(int position) {

    }

}