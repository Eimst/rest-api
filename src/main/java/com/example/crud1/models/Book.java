package com.example.crud1.models;


public class Book {

    private int id;
    private String title;
    private int publishYear;
    private String author;
    private int rating;

    public Book(int id, String title, int publishYear, String author, int rating) {
        this.id = id;
        this.title = title;
        this.publishYear = publishYear;
        this.author = author;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public String getAuthor() {
        return author;
    }

    public int getRating() {
        return rating;
    }

}
