package com.example.gark.entites;

public class Ressource {
    String id;
    String firstName;
    String lastName;
    String name;
    String image;
    RessourceType type;
    public Ressource() {
    }

    public Ressource(String id) {
        this.id = id;
    }
    //player
    public Ressource(String firstName, String lastName, String image, RessourceType type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.type = type;
    }
    //club
    public Ressource(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public RessourceType getType() {
        return type;
    }

    public void setType(RessourceType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Ressource{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", type=" + type +
                '}';
    }
}
