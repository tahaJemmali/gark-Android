package com.example.gark.entites;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Match implements Serializable {
    String id;
    Date start_date;
    Date end_date;
    Team team1;
    Team team2;
    List<MatchAction> goals;
    List<MatchAction> yellowCards;
    List<MatchAction> redCards;
    Team winner;
    ChallengeState state=ChallengeState.Pending;
    MatchType type=MatchType.Round;

    public Match() {
    }

    public Match(Date start_date, Team team1, Team team2) {
        this.start_date = start_date;
        this.team1 = team1;
        this.team2 = team2;
    }

    public Match(String id) {
        this.id=id;
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

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public List<MatchAction> getGoals() {
        return goals;
    }

    public void setGoals(List<MatchAction> goals) {
        this.goals = goals;
    }

    public List<MatchAction> getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(List<MatchAction> yellowCards) {
        this.yellowCards = yellowCards;
    }

    public List<MatchAction> getRedCards() {
        return redCards;
    }

    public void setRedCards(List<MatchAction> redCards) {
        this.redCards = redCards;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public ChallengeState getState() {
        return state;
    }

    public void setState(ChallengeState state) {
        this.state = state;
    }

    public MatchType getType() {
        return type;
    }

    public void setType(MatchType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", team1=" + team1 +
                ", team2=" + team2 +
                ", goals=" + goals +
                ", yellowCards=" + yellowCards +
                ", redCards=" + redCards +
                ", winner=" + winner +
                ", state=" + state +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id);
    }


}
