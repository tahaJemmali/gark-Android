package com.example.gark.entites;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Skills implements Serializable{
    String id;
    int pace;
    int shooting;
    int passing;
    int dribbling;
    int defending;
    int physical;
    int score;
    int rating;
    int goals;
    Role role;
    User player;
    Nationality nationality=Nationality.tunisia;
    List<Team> teams;
    String description;
    int age;
    public Skills(){
         pace=50;
         shooting=50;
         passing=50;
         dribbling=50;
         defending=50;
         physical=50;
         score=50;
         goals=0;
         rating=0;
         nationality=Nationality.tunisia;
        description=" ";
        age=18;
    }

    public Skills(Role role,User player) {
        this();
        this.role = role;
        this.player=player;
    }
    public Skills(String userId) {
        this.id=userId;
    }
    public Skills(String id, int pace, int shooting, int passing, int dribbling, int defending, int physical, int score, int goals, Role role, User player, Nationality nationality,int rating,String description,int age) {
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
        this.description=description;
        this.age=age;
    }

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }

    public int getShooting() {
        return shooting;
    }

    public void setShooting(int shooting) {
        this.shooting = shooting;
    }

    public int getPassing() {
        return passing;
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }

    public int getDribbling() {
        return dribbling;
    }

    public void setDribbling(int dribbling) {
        this.dribbling = dribbling;
    }

    public int getDefending() {
        return defending;
    }

    public void setDefending(int defending) {
        this.defending = defending;
    }

    public int getPhysical() {
        return physical;
    }

    public void setPhysical(int physical) {
        this.physical = physical;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
                ", teams=" + teams +
                ", age=" + age +
                ", description=" + description +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skills skills = (Skills) o;
        return Objects.equals(id, skills.id);
    }
}
