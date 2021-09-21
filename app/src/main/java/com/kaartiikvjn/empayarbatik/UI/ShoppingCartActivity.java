package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.data.CartItem;
import com.kaartiikvjn.empayarbatik.databinding.ActivityShoppingCartBinding;
import com.kaartiikvjn.empayarbatik.helper.CartItemAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class ShoppingCartActivity extends AppCompatActivity {
    private ActivityShoppingCartBinding binding;
    private ArrayList<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarShoppingCart.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        cartItems = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            cartItems.add(new CartItem(String.valueOf(i), String.valueOf(i), String.valueOf(i), getString(R.string.app_name), 10.00));
        }
        recyclerViewSetter();
        binding.proceed.setOnClickListener(v -> {
            startActivity(new Intent(ShoppingCartActivity.this, PaymentPage.class));
        });
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
        binding.shoppingCartRecyclerView.setLayoutManager(new LinearLayoutManager(ShoppingCartActivity.this));
        binding.shoppingCartRecyclerView.setAdapter(new CartItemAdapter(cartItems, ShoppingCartActivity.this));
    }

    public void onRemoveItemTapped(int position) {

    }

    public void onDecQuantityTapped(int position) {

    }

    public void onIncreaseQuantityTapped(int position) {

    }

}