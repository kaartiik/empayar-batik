package com.kaartiikvjn.empayarbatik.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.data.Coupon;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {
    ArrayList<Coupon> coupons;
    OnItemTapped onItemTapped;
    OnRemoveButtonTapped onRemoveButtonTapped;

    public CouponAdapter(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }

    public interface OnItemTapped {
        void onTap(int position);
    }

    public interface OnRemoveButtonTapped {
        void onTap(int position);
    }

    public void setOnRemoveButtonTappedListener(OnRemoveButtonTapped onRemoveButtonTapped) {
        this.onRemoveButtonTapped = onRemoveButtonTapped;
    }

    public void setOnItemTappedListener(OnItemTapped onItemTapped) {
        this.onItemTapped = onItemTapped;
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CouponViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_coupon_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position) {
        Coupon current = coupons.get(position);
        holder.couponTitle.setText(current.getCouponTitle());
        holder.itemView.setOnClickListener(v -> onItemTapped.onTap(position));
        holder.removeCoupon.setOnClickListener(v -> onRemoveButtonTapped.onTap(position));
    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

    static class CouponViewHolder extends RecyclerView.ViewHolder {
        TextView couponTitle;
        ImageButton removeCoupon;
        CouponViewHolder(View itemView) {
            super(itemView);
            couponTitle = itemView.findViewById(R.id.coupon_title);
            removeCoupon = itemView.findViewById(R.id.delete_coupon);
        }
    }
}
