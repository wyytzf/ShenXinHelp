package com.xd.shenxinhelp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/3/16.
 */

public class PKHistory implements Serializable{
    private int winTeamID;
    private int pkringID;
    private int cridits;
    private String date;
    private int type;
    private List<ParticipateTeam> participateTeam;

    public List<ParticipateTeam> getParticipateTeam() {
        return participateTeam;
    }

    public void setParticipateTeam(List<ParticipateTeam> participateTeam) {
        this.participateTeam = participateTeam;
    }

    public int getWinTeamID() {
        return winTeamID;
    }

    public void setWinTeamID(int winTeamID) {
        this.winTeamID = winTeamID;
    }

    public int getPkringID() {
        return pkringID;
    }

    public void setPkringID(int pkringID) {
        this.pkringID = pkringID;
    }

    public int getCridits() {
        return cridits;
    }

    public void setCridits(int cridits) {
        this.cridits = cridits;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
