package com.example.gark.entites;

import java.io.Serializable;
import java.util.Date;

public class Story implements Serializable {
    String id;
    String resource;
    int views=0;
    User creator;
    Date date_created;

    public Story(){

    }

    public Story(String resource, User creator, Date date_created) {
        this.resource = resource;
        this.creator = creator;
        this.date_created = date_created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getresource() {
        return resource;
    }

    public void setresource(String resource) {
        this.resource = resource;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
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

    @Override
    public String toString() {
        return "Story{" +
                "id='" + id + '\'' +
                ", resource='" + resource + '\'' +
                ", views=" + views +
                ", creator=" + creator +
                ", date_created=" + date_created +
                '}';
    }
}
