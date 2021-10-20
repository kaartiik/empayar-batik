package com.example.empayarbatik.data;

public class Coupon {
    String couponId,couponTitle;
    double discountPercentage;

    public Coupon(String couponId, String couponTitle, double discountPercentage) {
        this.couponId = couponId;
        this.couponTitle = couponTitle;
        this.discountPercentage = discountPercentage;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
