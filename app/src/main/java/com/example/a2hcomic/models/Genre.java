package com.example.a2hcomic.models;

public class Genre {
    private String id;
    private String title;

    public Genre(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Genre() {
        this("", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
