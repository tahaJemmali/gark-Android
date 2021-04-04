package com.example.gark.entites;

import java.util.Date;
import java.util.Objects;

public class MatchVote {
    String id;
    Skills voter;
    Skills votedOn;
    int pace;
    int shooting;
    int passing;
    int dribbling;
    int defending;
    int physical;
    int rating;
    Date vote_date;

    public MatchVote() {
    }

    public MatchVote(String id) {
        this.id = id;
    }

    public MatchVote(Skills voter, Skills votedOn, int pace, int shooting, int passing, int dribbling, int defending, int physical, int rating, Date vote_date) {
        this.voter = voter;
        this.votedOn = votedOn;
        this.pace = pace;
        this.shooting = shooting;
        this.passing = passing;
        this.dribbling = dribbling;
        this.defending = defending;
        this.physical = physical;
        this.rating = rating;
        this.vote_date = vote_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Skills getVoter() {
        return voter;
    }

    public void setVoter(Skills voter) {
        this.voter = voter;
    }

    public Skills getVotedOn() {
        return votedOn;
    }

    public void setVotedOn(Skills votedOn) {
        this.votedOn = votedOn;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getVote_date() {
        return vote_date;
    }

    public void setVote_date(Date vote_date) {
        this.vote_date = vote_date;
    }

    @Override
    public String toString() {
        return "MatchVote{" +
                "id='" + id + '\'' +
                ", voter=" + voter +
                ", votedOn=" + votedOn +
                ", pace=" + pace +
                ", shooting=" + shooting +
                ", passing=" + passing +
                ", dribbling=" + dribbling +
                ", defending=" + defending +
                ", physical=" + physical +
                ", rating=" + rating +
                ", vote_date=" + vote_date +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchVote that = (MatchVote) o;
        return Objects.equals(id, that.id);
    }
}
