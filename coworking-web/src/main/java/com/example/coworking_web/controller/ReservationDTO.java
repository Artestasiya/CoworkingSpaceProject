package com.example.coworking_web.controller;

public class ReservationDTO {
    private String userName;
    private String date;
    private String startTime;
    private String endTime;
    private int spaceId;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public int getSpaceId() { return spaceId; }
    public void setSpaceId(int spaceId) { this.spaceId = spaceId; }
}
