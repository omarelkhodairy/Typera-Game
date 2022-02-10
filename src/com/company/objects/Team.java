package com.company.objects;

import com.company.server.ServerSocketTask;

import java.io.Serializable;


public class Team implements Serializable {


    private ServerSocketTask member1;
    private ServerSocketTask member2;
    private int teamID;
    private double score = 0;
    private boolean isNewRecord = false;
    public Team(ServerSocketTask member1, ServerSocketTask member2, int teamID, double score) {
        this.member1 = member1;
        this.member2 = member2;
        this.teamID = teamID;
        this.score = score;
    }

    public Team() {
    }

    public ServerSocketTask getMember1() {
        return member1;
    }

    public void setMember1(ServerSocketTask member1) {
        this.member1 = member1;
    }

    public ServerSocketTask getMember2() {
        return member2;
    }

    public void setMember2(ServerSocketTask member2) {
        this.member2 = member2;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public void setNewRecord(boolean newRecord) {
        isNewRecord = newRecord;
    }
}






