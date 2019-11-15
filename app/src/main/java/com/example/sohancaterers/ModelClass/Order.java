package com.example.sohancaterers.ModelClass;

public class Order {
    private String item_name;
    private  int price;

    public Order() {
    }

    public Order(String item_name,  int price) {
        this.item_name = item_name;
        this.price = price;

    }



    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
