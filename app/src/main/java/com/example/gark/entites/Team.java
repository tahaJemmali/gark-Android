package com.example.gark.entites;

import java.io.Serializable;
import java.util.List;

public class Team implements Serializable {
    String id;
    String name;
    String image;
    Skills capitaine;
    String categorie;
    List<Skills> titulares;
    List<Skills> substitutes;
    int victories=0;
    int defeats=0;
    int points=0;
    int rating=0;


    public Team(){

    }

    public Team(String name, String image,String categorie, Skills capitaine, List<Skills> titulares, List<Skills> substitutes) {
        this.name = name;
        this.image = image;
        this.categorie = categorie;
        this.capitaine = capitaine;
        this.titulares = titulares;
        this.substitutes = substitutes;
    }

    public Team(String id, String name, String image, Skills capitaine, List<Skills> titulares, List<Skills> substitutes, int victories, int defeats, int points, int rating,String categorie) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.capitaine = capitaine;
        this.titulares = titulares;
        this.substitutes = substitutes;
        this.victories = victories;
        this.defeats = defeats;
        this.points = points;
        this.rating = rating;
        this.categorie = categorie;
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

    public Skills getCapitaine() {
        return capitaine;
    }

    public void setCapitaine(Skills capitaine) {
        this.capitaine = capitaine;
    }

    public List<Skills> getTitulares() {
        return titulares;
    }

    public void setTitulares(List<Skills> titulares) {
        this.titulares = titulares;
    }

    public List<Skills> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(List<Skills> substitutes) {
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

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
                ", categorie=" + categorie +
                ", rating=" + rating +
                '}';
    }
}
