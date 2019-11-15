package com.example.sohancaterers.ModelClass;

public class NoteMenu {
    public String menu,imageLink;


    public NoteMenu() {
    }

    public NoteMenu(String menu, String imageLink) {
        this.menu = menu;
        this.imageLink = imageLink;

    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink)
    {
        this.imageLink = imageLink;
    }



}
