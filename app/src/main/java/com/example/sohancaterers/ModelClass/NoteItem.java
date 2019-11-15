package com.example.sohancaterers.ModelClass;

public class NoteItem {
    public String item,url;
    public int price;

    public NoteItem() {
    }

    public NoteItem(String item, String url, int price) {
        this.item = item;
        this.url = url;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }




    public void setPrice(int price) {
        this.price = price;
    }
}
