package com.kaartiikvjn.empayarbatik.helper;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaartiikvjn.empayarbatik.UI.CategoryFragment;
import com.kaartiikvjn.empayarbatik.data.Item;
import com.kaartiikvjn.empayarbatik.databinding.LayoutItemTileBinding;
import com.kaartiikvjn.empayarbatik.utils.ImageHelper;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
    private ArrayList<Item> items;
    private CategoryFragment fragment;
    private Context context;
    private ImageHelper imageHelper;

    public ItemListAdapter(ArrayList<Item> items, CategoryFragment fragment, Context context) {
        this.items = items;
        this.fragment = fragment;
        this.context = context;
        imageHelper = new ImageHelper(context);
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
            fragment.onTileTapped(position);
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
