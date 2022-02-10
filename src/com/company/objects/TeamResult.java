package com.company.objects;

import java.io.Serializable;

public class TeamResult implements Serializable {
    private String member1;
    private String member2;
    private int teamID;
    private double score = 0;
    private boolean isNewRecord = false;

    public TeamResult() {
    }

    public TeamResult(String member1, String member2, int teamID, double score, boolean isNewRecord) {
        this.member1 = member1;
        this.member2 = member2;
        this.teamID = teamID;
        this.score = score;
        this.isNewRecord = isNewRecord;
    }

    public String getMember1() {
        return member1;
    }

    public void setMember1(String member1) {
        this.member1 = member1;
    }

    public String getMember2() {
        return member2;
    }

    public void setMember2(String member2) {
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
