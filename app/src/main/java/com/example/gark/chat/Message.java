package com.example.gark.chat;

import com.example.gark.entites.User;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    Date dateCreated;
    String senderId;
    String reciverId;
    String message;
    String urlImage;

    public Message() {
    }

    public Message( String message,String senderId,String reciverId) {
        this.senderId = senderId;
        this.reciverId = reciverId;
        this.message = message;
    }

    public Message( String message, String urlImage,String senderId,String reciverId) {
        this.senderId = senderId;
        this.message = message;
        this.reciverId = reciverId;
        this.urlImage = urlImage;
    }



    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getsenderId() {
        return senderId;
    }

    public void setsenderId(String senderId) {
        this.senderId = senderId;
    }
  //  @ServerTimestamp
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

    public String getreciverId() {
        return reciverId;
    }

    public void setreciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    @Override
    public String toString() {
        return "Message{" +

                ", dateCreated=" + dateCreated +
                ", senderId=" + senderId +
                ", reciverId=" + reciverId +
                ", message='" + message + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
