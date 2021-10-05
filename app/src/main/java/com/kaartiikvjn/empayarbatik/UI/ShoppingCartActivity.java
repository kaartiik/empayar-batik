package com.kaartiikvjn.empayarbatik.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.data.CartItem;
import com.kaartiikvjn.empayarbatik.databinding.ActivityShoppingCartBinding;
import com.kaartiikvjn.empayarbatik.helper.CartItemAdapter;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;

import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")
public class ShoppingCartActivity extends BaseActivity {
    private ActivityShoppingCartBinding binding;
    private ArrayList<CartItem> cartItems;
    private ArrayList<String> keys;
    private CartItemAdapter cartItemAdapter;
    private ChildEventListener mListener;
    private double price;
    private static final String FIVE_PER_CODE = "RT78YU";
    private static final String TEN_PER_CODE = "SE78RT";
    private static final String FIFTEEN_PER_CODE = "TYU908";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarShoppingCart.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        cartItems = new ArrayList<>();
        keys = new ArrayList<>();
        recyclerViewSetter();
        binding.couponEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 6) {
                    switch (s.toString())
                    {
                        case FIVE_PER_CODE:{
                            double discount = price*0.05;
                            price = price-discount;
                            setPrice(price);
                        }
                        break;
                        case TEN_PER_CODE:{
                            double discount = price*0.10;
                            price = price-discount;
                            setPrice(price);
                        }
                        break;
                        case FIFTEEN_PER_CODE:{
                            double discount = price*0.15;
                            price = price-discount;
                            setPrice(price);
                        }
                        break;
                        default: {
                            toast("Provided coupon code isn't valid");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.proceed.setOnClickListener(v -> {
            startActivity(new Intent(ShoppingCartActivity.this, PaymentPage.class));
        });
        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    keys.add(snapshot.getKey());
                    cartItems.add(new CartItem(
                            snapshot.getKey(),
                            Objects.requireNonNull(snapshot.child(Constants.cartItemId).getValue()).toString(),
                            Objects.requireNonNull(snapshot.child(Constants.cartItemQuantity).getValue()).toString(),
                            Objects.requireNonNull(snapshot.child(Constants.cartItemTitle).getValue()).toString(),
                            Double.parseDouble(Objects.requireNonNull(snapshot.child(Constants.cartItemPrice).getValue()).toString()),
                            Objects.requireNonNull(snapshot.child(Constants.cartItemSize).getValue()).toString()
                    ));
                    cartItemAdapter.notifyDataSetChanged();
                    calculatePrice();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int index = keys.indexOf(snapshot.getKey());
                cartItems.set(index, new CartItem(
                        snapshot.getKey(),
                        Objects.requireNonNull(snapshot.child(Constants.cartItemId).getValue()).toString(),
                        Objects.requireNonNull(snapshot.child(Constants.cartItemQuantity).getValue()).toString(),
                        Objects.requireNonNull(snapshot.child(Constants.cartItemTitle).getValue()).toString(),
                        Double.parseDouble(Objects.requireNonNull(snapshot.child(Constants.cartItemPrice).getValue()).toString()),
                        Objects.requireNonNull(snapshot.child(Constants.cartItemSize).getValue()).toString()
                ));
                cartItemAdapter.notifyDataSetChanged();
                calculatePrice();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = keys.indexOf(snapshot.getKey());
                keys.remove(index);
                cartItems.remove(index);
                cartItemAdapter.notifyDataSetChanged();
                calculatePrice();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        getDatabaseReference().child(Constants.shoppingCarts)
                .child(Objects.requireNonNull(getAuth().getUid())).addChildEventListener(mListener);

    }

    private void calculatePrice() {
        double price = 0.0;
        if (!cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
                price = price + (item.getPrice() * Integer.parseInt(item.getItemQuantity()));
            }
        }
        setPrice(price);
        this.price = price;
    }

    private void setPrice(double price)
    {
        binding.textViewPriceSubtotal.setText(String.format("RM %.2f", price));
        binding.textViewPriceTotal.setText(String.format("RM %.2f", price));
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
        getDatabaseReference().child(Constants.shoppingCarts)
                .child(Objects.requireNonNull(getAuth().getUid())).child(cartItems.get(position).getCartId()).removeValue();
        binding.couponEditText.setText("");
    }

    public void onDecQuantityTapped(int position) {
        int quantity = Integer.parseInt(cartItems.get(position).getItemQuantity());
        if (quantity == 1) {
            onRemoveItemTapped(position);
        } else {
            quantity = quantity - 1;
            getDatabaseReference().child(Constants.shoppingCarts)
                    .child(Objects.requireNonNull(getAuth().getUid())).child(cartItems.get(position).getCartId())
                    .child(Constants.cartItemQuantity).setValue(String.valueOf(quantity));
        }
        binding.couponEditText.setText("");
    }

    public void onIncreaseQuantityTapped(int position) {
        int quantity = Integer.parseInt(cartItems.get(position).getItemQuantity());
        quantity = quantity + 1;
        getDatabaseReference().child(Constants.shoppingCarts)
                .child(Objects.requireNonNull(getAuth().getUid())).child(cartItems.get(position).getCartId())
                .child(Constants.cartItemQuantity).setValue(String.valueOf(quantity));
        binding.couponEditText.setText("");
    }

}