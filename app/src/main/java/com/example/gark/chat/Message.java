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
    boolean vu=false;

    public Message() {
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

    public boolean isVu() {
        return vu;
    }

    public void setVu(boolean vu) {
        this.vu = vu;
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
