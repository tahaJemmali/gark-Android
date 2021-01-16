package com.example.gark.entites;

import java.io.Serializable;

public class Match implements Serializable {
    String id;
    String name;
    String start_time;
    String end_time;
    Team team1;
    Team team2;
    int goals=0;
    int point=0;
    Terrain terrain;
    boolean finished=false;

    public Match(){

    }

    //Start match
    public Match(String name, String start_time, Team team1, Team team2,Terrain terrain) {
        this.name = name;
        this.start_time = start_time;
        this.team1 = team1;
        this.team2 = team2;
        this.terrain=terrain;
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", team1=" + team1 +
                ", team2=" + team2 +
                ", goals=" + goals +
                ", point=" + point +
                ", terrain=" + terrain +
                ", finished=" + finished +
                '}';
    }
}
