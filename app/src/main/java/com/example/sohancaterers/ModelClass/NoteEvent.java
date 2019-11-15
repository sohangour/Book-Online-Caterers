package com.example.sohancaterers.ModelClass;

public class NoteEvent {
    public String eventname,eventimageLink,eventid;


    public NoteEvent() {
    }

    public NoteEvent(String eventname, String eventimageLink) {
        this.eventname = eventname;
        this.eventimageLink = eventimageLink;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventimageLink() {
        return eventimageLink;
    }

    public void setEventimageLink(String eventimageLink) {
        this.eventimageLink = eventimageLink;
    }
}
