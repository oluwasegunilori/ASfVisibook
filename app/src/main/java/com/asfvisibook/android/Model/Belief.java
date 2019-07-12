package com.example.asfvisibook.Model;

public class Belief {

    private int id;
    private String title, details, Bible_verse;

    public Belief() {
    }

    public Belief(int id, String title, String details, String bible_verse) {
        this.id = id;
        this.title = title;
        this.details = details;
        Bible_verse = bible_verse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getBible_verse() {
        return Bible_verse;
    }

    public void setBible_verse(String bible_verse) {
        Bible_verse = bible_verse;
    }
}
