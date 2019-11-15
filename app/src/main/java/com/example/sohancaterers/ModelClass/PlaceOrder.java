package com.example.sohancaterers.ModelClass;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.List;

public class PlaceOrder {
    private String documentId;
    private int total_member;
    private String event_name;
    private String event_date;
    private String event_time;
    private int total_amount;
    private String customer_name;
    private String customer_email;
    private String customer_contact;
    private String customer_address;
    private String venue_name;
    private String venue_address;
    private List<Order> ItemList;
    int per_member_price;
    public PlaceOrder() {
    }

    public PlaceOrder( int per_member_price,int total_member, String event_name, String event_date, String event_time, int total_amount, String customer_name, String customer_email, String customer_contact, String customer_address, String venue_name, String venue_address, List<Order> itemList) {
        this.total_member = total_member;
        this.per_member_price=per_member_price;
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_time = event_time;
        this.total_amount = total_amount;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_contact = customer_contact;
        this.customer_address = customer_address;
        this.venue_name = venue_name;
        this.venue_address = venue_address;
         ItemList = itemList;
    }


    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getPer_member_price() {
        return per_member_price;
    }

    public void setPer_member_price(int per_member_price) {
        this.per_member_price = per_member_price;
    }

    public int getTotal_member() {
        return total_member;
    }

    public void setTotal_member(int total_member) {
        this.total_member = total_member;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_contact() {
        return customer_contact;
    }

    public void setCustomer_contact(String customer_contact) {
        this.customer_contact = customer_contact;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getVenue_address() {
        return venue_address;
    }

    public void setVenue_address(String venue_address) {
        this.venue_address = venue_address;
    }

    public List<Order> getItemList() {
        return ItemList;
    }

    public void setItemList(List<Order> itemList) {
        ItemList = itemList;
    }
}
