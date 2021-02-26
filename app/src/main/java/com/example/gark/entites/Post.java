package com.example.gark.entites;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Post implements Serializable,Cloneable {
    String id;
    String title;
    String description;
    String image;
    User creator;
    List<User> likes;
    List<User> views;
    Date date_created=new Date();
    public Post(){

    }
    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public Post(String title, String description, String image, User creator) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.creator = creator;
    }

    public Post(String id, String title, String description, String image, User creator, Date date_created, List<User> likes, List<User> views) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.creator = creator;
        this.likes = likes;
        this.views = views;
        this.date_created=date_created;
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


    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    public List<User> getViews() {
        return views;
    }

    public void setViews(List<User> views) {
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
