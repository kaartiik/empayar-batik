package com.kaartiikvjn.empayarbatik.data;

public class CartItem {
    private String id;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private String itemId;
    private String itemQuantity;
    private String itemTitle;
    private Double price;

    public CartItem(String id, String itemId, String itemQuantity, String itemTitle, Double price) {
        this.id = id;
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
        this.itemTitle = itemTitle;
        this.price =  price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
