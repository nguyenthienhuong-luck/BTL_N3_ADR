package com.example.a2hcomic.models;

public class Comic {
    private String author_id,
            banner_url,
            description,
            id,
            img_url,
            title,
            user_id;
    private int create_at;

    public Comic() {

    }

    public Comic(String author_id, String banner_url, String description, String id, String img_url, String title, String user_id, int create_at) {
        this.author_id = author_id;
        this.banner_url = banner_url;
        this.description = description;
        this.id = id;
        this.img_url = img_url;
        this.title = title;
        this.user_id = user_id;
        this.create_at = create_at;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getCreate_at() {
        return create_at;
    }

    public void setCreate_at(int create_at) {
        this.create_at = create_at;
    }
}
