package com.example.gark.entites;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Team implements Serializable {
    String id;
    String name;
    String image;
    Skills capitaine;
    Categorie categorie;
    List<Skills> titulares;
    List<Skills> substitutes;
    int victories=0;
    int defeats=0;
    int points=0;
    int rating=0;
    int draws=0;
    Nationality nationality=Nationality.tunisia;
    String description;
    Date date_created;


    public Team(){
         victories=0;
         defeats=0;
         points=0;
         rating=0;
         draws=0;
    }
    public Team(String id){
    this.id=id;
    }

    public Team(String name, String image,Categorie categorie, Skills capitaine, List<Skills> titulares, List<Skills> substitutes,Nationality nationality) {
        this.name = name;
        this.image = image;
        this.categorie = categorie;
        this.capitaine = capitaine;
        this.titulares = titulares;
        this.substitutes = substitutes;
        this.nationality = nationality;
    }

    public Team(String id, String name, String image, Skills capitaine, List<Skills> titulares, List<Skills> substitutes, int victories, int defeats, int points, int rating,Categorie categorie,
                Nationality nationality,String description,Date date_created,int draws) {
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
        this.nationality = nationality;
        this.description = description;
        this.date_created = date_created;
        this.draws = draws;
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

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
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
                ", nationality=" + nationality +
                ", description=" + description +
                ", date_created=" + date_created +
                ", draws=" + draws +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id.equals(team.id);
    }

}
