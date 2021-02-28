package com.example.gark.chat;

import com.example.gark.entites.User;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    String id;
    Date dateCreated;
    User userSender;
    String message;
    String urlImage;

    public Message() {
    }

    public Message( String message,User userSender) {
        this.userSender = userSender;
        this.message = message;
    }

    public Message( String message, String urlImage,User userSender) {
        this.userSender = userSender;
        this.message = message;
        this.urlImage = urlImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }
    @ServerTimestamp
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
