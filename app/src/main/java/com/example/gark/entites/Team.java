package com.example.gark.entites;

import java.io.Serializable;
import java.util.List;

public class Team implements Serializable {
    String id;
    String name;
    String image;
    User capitaine;
    List<User> titulares;
    List<User> substitutes;
    int victories=0;
    int defeats=0;
    int points=0;
    int rating=0;


    public Team(){

    }

    public Team(String name, String image, User capitaine, List<User> titulares, List<User> substitutes) {
        this.name = name;
        this.image = image;
        this.capitaine = capitaine;
        this.titulares = titulares;
        this.substitutes = substitutes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getCapitaine() {
        return capitaine;
    }

    public void setCapitaine(User capitaine) {
        this.capitaine = capitaine;
    }

    public List<User> getTitulares() {
        return titulares;
    }

    public void setTitulares(List<User> titulares) {
        this.titulares = titulares;
    }

    public List<User> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(List<User> substitutes) {
        this.substitutes = substitutes;
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getDefeats() {
        return defeats;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", capitaine=" + capitaine +
                ", titulares=" + titulares +
                ", substitutes=" + substitutes +
                ", victories=" + victories +
                ", points=" + points +
                ", defeats=" + defeats +
                '}';
    }
}
