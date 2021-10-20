package com.example.empayarbatik.helper;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.empayarbatik.data.Item;
import com.example.empayarbatik.databinding.LayoutItemTileBinding;
import com.example.empayarbatik.utils.ImageHelper;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
    private ArrayList<Item> items;
    private Context context;
    private ImageHelper imageHelper;
    private OnItemTapped onItemTapped;

    public ItemListAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
        imageHelper = new ImageHelper(context);
    }

    public interface OnItemTapped {
        void onTap(int position);
    }

    public void setOnItemTappedListener(OnItemTapped onItemTapped) {
        this.onItemTapped = onItemTapped;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutItemTileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item current = items.get(position);
        holder.root.itemListTitle.setText(current.getItemTitle());
        holder.root.itemListPrice.setText("RM " + String.format("%.2f", current.getItemPrice()));
        holder.root.itemListPrice.setPaintFlags(holder.root.itemListPrice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        imageHelper.loadCloudImage(current.getPhotoUrl(), holder.root.itemListImageView);
        holder.itemView.setOnClickListener(v -> {
            onItemTapped.onTap(position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        LayoutItemTileBinding root;

        public ItemViewHolder(@NonNull LayoutItemTileBinding binding) {
            super(binding.getRoot());
            root = binding;
        }
    }
}
