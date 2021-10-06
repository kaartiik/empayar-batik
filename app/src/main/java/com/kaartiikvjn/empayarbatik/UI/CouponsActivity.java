package com.kaartiikvjn.empayarbatik.UI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.data.Coupon;
import com.kaartiikvjn.empayarbatik.databinding.ActivityCouponsBinding;
import com.kaartiikvjn.empayarbatik.helper.CouponAdapter;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")
public class CouponsActivity extends BaseActivity {
    private ActivityCouponsBinding binding;
    private ChildEventListener mListener;
    private ArrayList<Coupon> coupons;
    private ArrayList<String> keys;
    private CouponAdapter couponAdapter;
    private String couponCode = "";
    private String couponDiscount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCouponsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.couponsToolbar.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        recyclerViewSetter();
        binding.addCouponFab.setOnClickListener(v -> {
            addCoupon(null);
        });
        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                keys.add(snapshot.getKey());
                coupons.add(new Coupon(
                        snapshot.getKey(),
                        Objects.requireNonNull(snapshot.child(Constants.couponTitle).getValue()).toString(),
                        Double.parseDouble(Objects.requireNonNull(snapshot.child(Constants.couponDiscount).getValue()).toString())
                ));
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int index = keys.indexOf(snapshot.getKey());
                coupons.set(index, new Coupon(
                        snapshot.getKey(),
                        Objects.requireNonNull(snapshot.child(Constants.couponTitle).getValue()).toString(),
                        Double.parseDouble(Objects.requireNonNull(snapshot.child(Constants.couponDiscount).getValue()).toString())
                ));
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = keys.indexOf(snapshot.getKey());
                keys.remove(index);
                coupons.remove(index);
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        getDatabaseReference().child(Constants.coupons).addChildEventListener(mListener);
    }

    @SuppressLint("InflateParams")
    private void addCoupon(String couponId) {
        Dialog dialog = new Dialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_coupon_dialog, null, false);
        dialog.setContentView(dialogView);
        TextInputLayout couponTitle = dialogView.findViewById(R.id.coupon_title_editText);
        TextInputLayout couponDiscount = dialogView.findViewById(R.id.coupon_discount_editText);
        Objects.requireNonNull(couponTitle.getEditText()).setText(this.couponCode);
        Objects.requireNonNull(couponDiscount.getEditText()).setText(this.couponDiscount);
        Button saveButton = dialogView.findViewById(R.id.save_coupon);
        saveButton.setOnClickListener(v -> {
            if (isValidEverything(couponTitle, couponDiscount)) {
                if (couponId == null) {
                    showProgressDialog("Adding new coupon to database");
                    String key = getDatabaseReference().child(Constants.coupons).push().getKey();
                    HashMap<String, String> coupon = new HashMap<>();
                    coupon.put(Constants.couponTitle, Objects.requireNonNull(couponTitle.getEditText()).getText().toString());
                    coupon.put(Constants.couponDiscount, Objects.requireNonNull(couponDiscount.getEditText()).getText().toString());
                    assert key != null;
                    getDatabaseReference().child(Constants.coupons).child(key).setValue(coupon)
                            .addOnSuccessListener(unused -> {
                                hideProgressDialog();
                            }).addOnFailureListener(e -> {
                        hideProgressDialog();
                        toast("Failed to add coupon");
                    });
                } else {
                    showProgressDialog("Updating existing coupon to database");
                    HashMap<String, String> coupon = new HashMap<>();
                    coupon.put(Constants.couponTitle, Objects.requireNonNull(couponTitle.getEditText()).getText().toString());
                    coupon.put(Constants.couponDiscount, Objects.requireNonNull(couponDiscount.getEditText()).getText().toString());
                    getDatabaseReference().child(Constants.coupons).child(couponId).setValue(coupon)
                            .addOnSuccessListener(unused -> {
                                hideProgressDialog();
                            }).addOnFailureListener(e -> {
                        hideProgressDialog();
                        toast("Failed to update coupon");
                    });
                }
            dialog.dismiss();
            }
        });
        dialog.show();
    }

    private Boolean isValidEverything(TextInputLayout first, TextInputLayout second) {
        if (Objects.requireNonNull(first.getEditText()).getText().toString().isEmpty()) {
            first.getEditText().requestFocus();
            first.getEditText().setError("Please enter coupon title");
            return false;
        } else if (Objects.requireNonNull(second.getEditText()).getText().toString().isEmpty()) {
            second.getEditText().requestFocus();
            second.getEditText().setError("Please enter coupon discount");
            return false;
        } else
            return true;
    }

    private void recyclerViewSetter() {
        coupons = new ArrayList<>();
        keys = new ArrayList<>();
        couponAdapter = new CouponAdapter(coupons);
        binding.couponsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.couponsRecyclerView.setAdapter(couponAdapter);
        couponAdapter.setOnItemTappedListener(position -> {
            couponCode = coupons.get(position).getCouponTitle();
            couponDiscount = String.valueOf(coupons.get(position).getDiscountPercentage());
            addCoupon(coupons.get(position).getCouponId());
        });
        couponAdapter.setOnRemoveButtonTappedListener(position -> {
            getDatabaseReference().child(Constants.coupons).child(coupons.get(position).getCouponId()).removeValue();
        });
    }

    private void toolbarSetter(ActionBar actionBar) {
        actionBar.setTitle(getString(R.string.coupons));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}