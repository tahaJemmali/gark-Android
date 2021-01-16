package com.example.gark.entites;

import java.io.Serializable;
import java.util.Date;

public class Availability implements Serializable {
    String id;
    Date start_date;
    Date end_date;
    Terrain terrain;
    public Availability(){

    }

    public Availability(Date start_date, Date end_date, Terrain terrain) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.terrain = terrain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    @Override
    public String toString() {
        return "Availability{" +
                "id='" + id + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", terrain=" + terrain +
                '}';
    }
}
