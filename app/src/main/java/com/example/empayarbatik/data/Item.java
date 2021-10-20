package com.example.empayarbatik.data;

import java.util.ArrayList;

public class Item {
    private String itemId, itemTitle, photoUrl, category, material, specialTraits;
    private ArrayList<String> sizes;
    private Double itemPrice;

    public Item(String itemId, String itemTitle, String photoUrl, String category, String material, String specialTraits, ArrayList<String> sizes, Double itemPrice) {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.photoUrl = photoUrl;
        this.category = category;
        this.material = material;
        this.specialTraits = specialTraits;
        this.sizes = sizes;
        this.itemPrice = itemPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSpecialTraits() {
        return specialTraits;
    }

    public void setSpecialTraits(String specialTraits) {
        this.specialTraits = specialTraits;
    }

    public ArrayList<String> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<String> sizes) {
        this.sizes = sizes;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
