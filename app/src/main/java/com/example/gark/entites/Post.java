package com.example.gark.entites;

import java.io.Serializable;

public class Post implements Serializable {
    String id;
    String title;
    String description;
    String image;
    User creator;
    int likes=0;
    public Post(){

    }

    public Post(String title, String description, String image, User creator) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.creator = creator;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", creator=" + creator +
                ", likes=" + likes +
                '}';
    }
}
