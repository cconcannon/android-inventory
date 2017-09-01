package com.example.vn0mrky.inventory;

/**
 * Created by vn0mrky on 6/28/17.
 */

public class Item {
    private String mName;
    private String mDescription;
    private int mQuantity;
    private int mPrice;
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    private static final int NO_IMAGE_PROVIDED = -1;

    public Item(String name, String description, int quantity, int price, int imageResourceId) {
        mName = name;
        mDescription = description;
        mQuantity = quantity;
        mImageResourceId = imageResourceId;
        mPrice = price;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public int getPrice() { return mPrice; }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
