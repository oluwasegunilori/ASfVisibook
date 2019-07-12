package com.example.asfvisibook.Model;

import java.util.Date;

public class ArticleClass {
    private String title, details, author, image;
    private String date;

    public ArticleClass(String title, String details, String author, String image, String date) {
        this.title = title;
        this.details = details;
        this.author = author;
        this.image = image;
        this.date = date;
    }

    public ArticleClass() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String  getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
