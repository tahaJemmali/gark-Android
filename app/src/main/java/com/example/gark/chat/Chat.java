package com.example.gark.chat;

import com.example.gark.entites.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Chat {
    String id;
    User user1;
    User user2;
    Date date_created;
    ArrayList<Message> messages = new ArrayList<Message>();

    public Chat() {

    }

    public Chat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public Chat(String id, User user1, User user2, Date date_created) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return (Objects.equals(user1, chat.user1) &&
                Objects.equals(user2, chat.user2)) || (Objects.equals(user2, chat.user1) &&
                Objects.equals(user1, chat.user2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }

   /* @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", user1=" + user1 +
                ", user2=" + user2 +
                ", date_created=" + date_created +
                ", messages=" + messages +
                '}';
    }*/
}
