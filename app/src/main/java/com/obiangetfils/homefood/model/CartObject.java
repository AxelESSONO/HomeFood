package com.obiangetfils.homefood.model;

public class CartObject {

    private DishItem dishItem;
    private String quantity;
    private String userId, userName;

    public CartObject() {
    }

    public CartObject(DishItem dishItem, String quantity, String userId, String userName) {
        this.dishItem = dishItem;
        this.quantity = quantity;
        this.userId = userId;
        this.userName = userName;
    }

    public DishItem getDishItem() {
        return dishItem;
    }

    public void setDishItem(DishItem dishItem) {
        this.dishItem = dishItem;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
