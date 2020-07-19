package com.obiangetfils.homefood.model;

import java.util.ArrayList;

public class DishItem {

    private String mImageUrls;
    private String mNames;
    private String mPrices;

    public DishItem(String mImageUrls, String mNames, String mPrices) {
        this.mImageUrls = mImageUrls;
        this.mNames = mNames;
        this.mPrices = mPrices;
    }

    public String getmImageUrls() {
        return mImageUrls;
    }

    public void setmImageUrls(String mImageUrls) {
        this.mImageUrls = mImageUrls;
    }

    public String getmNames() {
        return mNames;
    }

    public void setmNames(String mNames) {
        this.mNames = mNames;
    }

    public String getmPrices() {
        return mPrices;
    }

    public void setmPrices(String mPrices) {
        this.mPrices = mPrices;
    }
}
