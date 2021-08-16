package com.example.profile2_Admin.model;

public class HostelOccupant {
    String name;
    String department;
    String level;
    String picture;
    String phoneNum;
    String Gender;
    String StateOfOrigin;

    public HostelOccupant(String name, String department, String level, String picture, String phoneNum, String gender, String stateOfOrigin) {
        this.name = name;
        this.department = department;
        this.level = level;
        this.picture = picture;
        this.phoneNum = phoneNum;
        Gender = gender;
        StateOfOrigin = stateOfOrigin;
    }

    public HostelOccupant(String n, String d, String r, String u, String p) {
        this.name = n;
        this.department = d;
        this.level = r;
        this.picture = u;
        this.phoneNum = p;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getLevel() {
        return level;
    }

    public String getPicture() {
        return picture;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getGender() {
        return Gender;
    }

    public String getStateOfOrigin() {
        return StateOfOrigin;
    }
}
