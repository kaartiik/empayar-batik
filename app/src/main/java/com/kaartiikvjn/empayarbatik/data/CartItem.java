package com.kaartiikvjn.empayarbatik.data;

public class CartItem {
    private String cartId;
    private String itemId;
    private String itemQuantity;
    private String itemTitle;
    private Double price;
    private String size;

    public CartItem(String cartId, String itemId, String itemQuantity, String itemTitle, Double price, String size) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
        this.itemTitle = itemTitle;
        this.price =  price;
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;

    }
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
