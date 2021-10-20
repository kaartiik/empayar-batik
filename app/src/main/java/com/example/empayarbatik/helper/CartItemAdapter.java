package com.example.empayarbatik.helper;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.empayarbatik.UI.ShoppingCartActivity;
import com.example.empayarbatik.data.CartItem;
import com.example.empayarbatik.databinding.CartListLayoutBinding;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private final ArrayList<CartItem> items;
    private final ShoppingCartActivity activity;

    public CartItemAdapter(ArrayList<CartItem> items, ShoppingCartActivity activity) {
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartListLayoutBinding binding = CartListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartItemViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem current = items.get(position);
        holder.binding.cartItemTitle.setText(current.getItemTitle());
        holder.binding.cartItemQuantity.setText(current.getItemQuantity());
        holder.binding.cartItemPrice.setText("RM " + String.format("%.2f", current.getPrice()));
        holder.binding.cartRemoveButton.setOnClickListener(v -> {
            activity.onRemoveItemTapped(position);
        });
        holder.binding.cartItemQuantityRemove.setOnClickListener(v -> {
            activity.onDecQuantityTapped(position);
        });
        holder.binding.cartItemQuantityAdd.setOnClickListener(v -> {
            activity.onIncreaseQuantityTapped(position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        CartListLayoutBinding binding;

        public CartItemViewHolder(@NonNull CartListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
