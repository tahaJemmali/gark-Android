package com.example.gark.entites;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Challenge implements Serializable {
    String id;
    String name;
    Date start_date;
    Date end_date;
    Date date_created;
    int maxNumberOfTeams;
    List<Team> teams;
    List<Match> matches;
    Team winner;
    float prize;
    String location;
    String description;
    String image;
    User creator;
    Terrain terrain;
    ChallengeType type;
    ChallengeState state;

    public Challenge() {
    }

    public Challenge(String name, Date start_date, Date end_date, Date date_created, int maxNumberOfTeams, List<Team> teams, List<Match> matches, Team winner, float prize, String location, String description, String image, User creator, Terrain terrain, ChallengeType type,ChallengeState state) {
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.date_created = date_created;
        this.maxNumberOfTeams = maxNumberOfTeams;
        this.teams = teams;
        this.matches = matches;
        this.winner = winner;
        this.prize = prize;
        this.location = location;
        this.description = description;
        this.image = image;
        this.creator = creator;
        this.terrain = terrain;
        this.type = type;
        this.state=state;
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

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public int getMaxNumberOfTeams() {
        return maxNumberOfTeams;
    }

    public void setMaxNumberOfTeams(int maxNumberOfTeams) {
        this.maxNumberOfTeams = maxNumberOfTeams;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public float getPrize() {
        return prize;
    }

    public void setPrize(float prize) {
        this.prize = prize;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public ChallengeType getType() {
        return type;
    }

    public void setType(ChallengeType type) {
        this.type = type;
    }

    public ChallengeState getState() {
        return state;
    }

    public void setState(ChallengeState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", date_created=" + date_created +
                ", maxNumberOfTeams=" + maxNumberOfTeams +
                ", teams=" + teams +
                ", matches=" + matches +
                ", winner=" + winner +
                ", prize=" + prize +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", creator=" + creator +
                ", terrain=" + terrain +
                ", type=" + type +
                ", state=" + state +
                '}';
    }
}
