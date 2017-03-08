package com.xd.shenxinhelp.model;

/**
 * Created by MMY on 2017/2/15.
 */

public class User {
    private String name;
    private String photoUrl;
    private String age;
    private String sex;
    private String height;
    private String weight;
    private String credits;
    private String health_degree;
    private String level;
    private String class_id;
    private String school_id;

    public void setAge(String age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public void setHealth_degree(String health_degree) {
        this.health_degree = health_degree;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getCredits() {
        return credits;
    }

    public String getHealth_degree() {
        return health_degree;
    }

    public String getLevel() {
        return level;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
