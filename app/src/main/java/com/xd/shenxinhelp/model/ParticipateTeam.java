package com.xd.shenxinhelp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/3/18.
 */

public class ParticipateTeam implements Serializable{
    private  String teamId;
    private  String title;
    private  List<Team> students;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public List<Team> getStudents() {
        return students;
    }

    public void setStudents(List<Team> students) {
        this.students = students;
    }




}
