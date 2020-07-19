package com.obiangetfils.homefood.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DishItem implements Parcelable {

    private String dishName, dishDescription, dishPrice, dishCategory, dishUri, dishKey;

    public DishItem() {
    }

    public DishItem(String dishName, String dishDescription, String dishPrice, String dishCategory, String dishUri, String dishKey) {
        this.dishName = dishName;
        this.dishDescription = dishDescription;
        this.dishPrice = dishPrice;
        this.dishCategory = dishCategory;
        this.dishUri = dishUri;
        this.dishKey = dishKey;
    }

    protected DishItem(Parcel in) {
        dishName = in.readString();
        dishDescription = in.readString();
        dishPrice = in.readString();
        dishCategory = in.readString();
        dishUri = in.readString();
        dishKey = in.readString();
    }

    public static final Creator<DishItem> CREATOR = new Creator<DishItem>() {
        @Override
        public DishItem createFromParcel(Parcel in) {
            return new DishItem(in);
        }

        @Override
        public DishItem[] newArray(int size) {
            return new DishItem[size];
        }
    };

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public String getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
    }

    public String getDishCategory() {
        return dishCategory;
    }

    public void setDishCategory(String dishCategory) {
        this.dishCategory = dishCategory;
    }

    public String getDishUri() {
        return dishUri;
    }

    public void setDishUri(String dishUri) {
        this.dishUri = dishUri;
    }

    public String getDishKey() {
        return dishKey;
    }

    public void setDishKey(String dishKey) {
        this.dishKey = dishKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dishName);
        dest.writeString(dishDescription);
        dest.writeString(dishPrice);
        dest.writeString(dishCategory);
        dest.writeString(dishUri);
        dest.writeString(dishKey);
    }
}
