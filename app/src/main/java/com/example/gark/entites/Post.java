package com.example.gark.entites;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    String id;
    String title;
    String description;
    String image;
    User creator;
    int likes=0;
    int views=0;
    Date date_created=new Date();
    public Post(){

    }

    public Post(String title, String description, String image, User creator) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.creator = creator;
    }

    public Post(String id, String title, String description, String image, User creator, int likes, Date date_created,int views) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.creator = creator;
        this.likes = likes;
        this.views = views;
        this.date_created = date_created;
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

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", creator=" + creator +
                ", date_created=" + date_created +
                ", likes=" + likes +
                ", views=" + views +
                '}';
    }
}
