package com.example.empayarbatik.UI;

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
import com.google.firebase.database.ValueEventListener;
import com.example.empayarbatik.R;
import com.example.empayarbatik.data.CartItem;
import com.example.empayarbatik.data.Coupon;
import com.example.empayarbatik.databinding.ActivityShoppingCartBinding;
import com.example.empayarbatik.helper.CartItemAdapter;
import com.example.empayarbatik.utils.BaseActivity;
import com.example.empayarbatik.utils.Constants;

import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")
public class ShoppingCartActivity extends BaseActivity {
    private ActivityShoppingCartBinding binding;
    private ArrayList<CartItem> cartItems;
    private ArrayList<String> keys;
    private ArrayList<Coupon> coupons;
    private CartItemAdapter cartItemAdapter;
    private ChildEventListener mListener;
    private double price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarShoppingCart.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        cartItems = new ArrayList<>();
        keys = new ArrayList<>();
        coupons = new ArrayList<>();
        recyclerViewSetter();
        showProgressDialog("Loading coupons");
        getDatabaseReference().child(Constants.coupons).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hideProgressDialog();
                for (DataSnapshot coupon : snapshot.getChildren()) {
                    coupons.add(new Coupon(
                            coupon.getKey(),
                            Objects.requireNonNull(coupon.child(Constants.couponTitle).getValue()).toString(),
                            Double.parseDouble(Objects.requireNonNull(coupon.child(Constants.couponDiscount).getValue()).toString())
                    ));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressDialog();
                toast("Failed to load coupon items");
            }
        });
        binding.couponEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 6) {
                    for (Coupon coupon : coupons) {
                        if (coupon.getCouponTitle().equals(s.toString())) {

                            double discount = price * coupon.getDiscountPercentage() / 100;
                            price = price - discount;
                            setPrice(price);
                        } else {
                            if (coupons.indexOf(coupon) == coupons.size() - 1) {
                                //REACHED END
                                toast("Invalid coupon code");
                            }
                        }
                    }
                } else
                    calculatePrice();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.proceed.setOnClickListener(v -> {
            saveCartItem(cartItems);
            savePreferenceFloat(Constants.orderPrice, Float.valueOf(String.valueOf(price)));
            startActivity(new Intent(ShoppingCartActivity.this, PaymentPage.class));
            finishAffinity();
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

    private void setPrice(double price) {
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