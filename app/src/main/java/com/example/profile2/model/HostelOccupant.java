package com.example.profile2.model;

public class HostelOccupant {
    String name;
    String department;
    String level;
    int picture;
    String phoneNum;

    public HostelOccupant(String pName, String pDepartment, String pLevel, int pPicture,String pPhoneNum) {
        name = pName;
        department = pDepartment;
        level = pLevel;
        picture = pPicture;
        phoneNum = pPhoneNum;
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

    public int getPicture() {
        return picture;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
}