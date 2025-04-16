package com.example.a2hcomic.models;

public class Slider {
    private String id, img_url;

    public Slider() {
    }

    public Slider(String id, String img_url) {
        this.id = id;
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
