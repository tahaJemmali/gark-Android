package com.example.gark.entites;

import java.util.Date;
import java.util.Objects;

public class MatchAction {
    String id;
    Skills player;
    Challenge challenge;
    Match match;
    Team team;
    Date date;
    MatchActionType type;

    public MatchAction(String id) {
        this.id=id;
    }

    public MatchAction(Skills player, Challenge challenge, Match match, Team team, Date date,MatchActionType type) {
        this.player = player;
        this.challenge = challenge;
        this.match = match;
        this.team = team;
        this.date = date;
        this.type=type;
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

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MatchActionType getType() {
        return type;
    }

    public void setType(MatchActionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MatchAction{" +
                "id='" + id + '\'' +
                ", player=" + player +
                ", challenge=" + challenge +
                ", match=" + match +
                ", team=" + team +
                ", date=" + date +
                ", type=" + type +
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
