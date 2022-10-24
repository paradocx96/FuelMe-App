package com.example.fuelme.models;

/*
* IT19014128
* A.M.W.W.R.L. Wataketiya
*
* Model class for queue log item
* */

public class QueueLogItem {
    private String id;
    private String customerUsername;
    private String stationId;
    private String stationLicense;
    private String stationName;
    private String queue;
    private String action;
    private String refuelStatus;
    private int year;
    private int month;
    private int dayNumber;
    private int hour;
    private int minute;
    private int second;


    public QueueLogItem() {
    }

    public QueueLogItem(String id, String customerUsername, String stationId, String stationLicense, String stationName,
                        String queue, String action, String refuelStatus,
                        int year, int month, int dayNumber, int hour, int minute, int second) {
        this.id = id;
        this.customerUsername = customerUsername;
        this.stationId = stationId;
        this.stationLicense = stationLicense;
        this.stationName = stationName;
        this.queue = queue;
        this.action = action;
        this.refuelStatus = refuelStatus;
        this.year = year;
        this.month = month;
        this.dayNumber = dayNumber;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationLicense() {
        return stationLicense;
    }

    public void setStationLicense(String stationLicense) {
        this.stationLicense = stationLicense;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRefuelStatus() {
        return refuelStatus;
    }

    public void setRefuelStatus(String refuelStatus) {
        this.refuelStatus = refuelStatus;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "QueueLogItem{" +
                "id='" + id + '\'' +
                ", customerUsername='" + customerUsername + '\'' +
                ", stationId='" + stationId + '\'' +
                ", stationLicense='" + stationLicense + '\'' +
                ", stationName='" + stationName + '\'' +
                ", queue='" + queue + '\'' +
                ", action='" + action + '\'' +
                ", refuelStatus='" + refuelStatus + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", dayNumber=" + dayNumber +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }
}
