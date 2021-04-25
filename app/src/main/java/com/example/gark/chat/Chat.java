package com.example.gark.chat;

import com.example.gark.entites.User;

import java.util.ArrayList;
import java.util.Date;

public class Chat {
    String id;
    ArrayList<Message> messages=new ArrayList<Message>();
    public Chat() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", messages size =" + messages.size() +
                '}';
    }
}
