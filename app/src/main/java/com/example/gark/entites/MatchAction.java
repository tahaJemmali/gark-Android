package com.example.gark.entites;

import java.util.Date;
import java.util.Objects;

public class MatchAction {
    String id;
    Skills player;
    Date date;

    public MatchAction(String id) {
        this.id=id;
    }

    public MatchAction(Skills player, Date date) {
        this.player = player;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Skills getPlayer() {
        return player;
    }

    public void setPlayer(Skills player) {
        this.player = player;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MatchAction{" +
                "id='" + id + '\'' +
                ", player=" + player +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchAction that = (MatchAction) o;
        return Objects.equals(id, that.id);
    }
}
