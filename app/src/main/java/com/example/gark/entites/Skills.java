package com.example.gark.entites;

import java.io.Serializable;

public class Skills implements Serializable{
    String id;
    byte pace=50;
    byte shooting=50;
    byte passing=50;
    byte dribbling=50;
    byte defending=50;
    byte physical=50;
    byte score=50;
    int rating=0;
    int goals=0;
    Role role;
    User player;
    Nationality nationality=Nationality.tunisia;

    public Skills(){
         pace=50;
         shooting=50;
         passing=50;
         dribbling=50;
         defending=50;
         physical=50;
         score=50;
         goals=0;
         nationality=Nationality.tunisia;
    }

    public Skills(Role role,User player) {
        super();
        this.role = role;
        this.player=player;
    }

    public Skills(String id, byte pace, byte shooting, byte passing, byte dribbling, byte defending, byte physical, byte score, int goals, Role role, User player, Nationality nationality,int rating) {
        this.id = id;
        this.pace = pace;
        this.shooting = shooting;
        this.passing = passing;
        this.dribbling = dribbling;
        this.defending = defending;
        this.physical = physical;
        this.score = score;
        this.goals = goals;
        this.role = role;
        this.player = player;
        this.nationality = nationality;
        this.rating = rating;
    }

    public byte getPace() {
        return pace;
    }

    public void setPace(byte pace) {
        this.pace = pace;
    }

    public byte getShooting() {
        return shooting;
    }

    public void setShooting(byte shooting) {
        this.shooting = shooting;
    }

    public byte getPassing() {
        return passing;
    }

    public void setPassing(byte passing) {
        this.passing = passing;
    }

    public byte getDribbling() {
        return dribbling;
    }

    public void setDribbling(byte dribbling) {
        this.dribbling = dribbling;
    }

    public byte getDefending() {
        return defending;
    }

    public void setDefending(byte defending) {
        this.defending = defending;
    }

    public byte getPhysical() {
        return physical;
    }

    public void setPhysical(byte physical) {
        this.physical = physical;
    }

    public byte getScore() {
        return score;
    }

    public void setScore(byte score) {
        this.score = score;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Skills{" +
                "id='" + id + '\'' +
                ", pace=" + pace +
                ", shooting=" + shooting +
                ", passing=" + passing +
                ", dribbling=" + dribbling +
                ", defending=" + defending +
                ", physical=" + physical +
                ", score=" + score +
                ", goals=" + goals +
                ", role=" + role +
                ", player=" + player +
                ", nationality=" + nationality +
                ", rating=" + rating +
                '}';
    }
}
