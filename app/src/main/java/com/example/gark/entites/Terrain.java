package com.example.gark.entites;

import java.io.Serializable;
import java.util.List;

public class Terrain implements Serializable {
    String id;
    String nom;
    String localisation;
    String image;
    List <Availability> availabilities;

    public Terrain(){

    }
    public Terrain(String nom,String localisation,List <Availability> availabilities){
        this.nom=nom;
        this.localisation=localisation;
        this.availabilities=availabilities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Terrain{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", localisation='" + localisation + '\'' +
                ", image='" + image + '\'' +
                ", availabilities='" + availabilities + '\'' +
                '}';
    }
}
