package com.example.fuelme.models;

public class FuelStationLogItem {
    private String id;
    private String stationId;
    private String fuelType;
    private String fuelStatus;
    private int year;
    private int month;
    private int dayNumber;
    private int hour;
    private int minute;
    private int second;

    public FuelStationLogItem() {
    }

    public FuelStationLogItem(String id, String stationId, String fuelType, String fuelStatus, int year, int month, int dayNumber, int hour, int minute, int second) {
        this.id = id;
        this.stationId = stationId;
        this.fuelType = fuelType;
        this.fuelStatus = fuelStatus;
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

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFuelStatus() {
        return fuelStatus;
    }

    public void setFuelStatus(String fuelStatus) {
        this.fuelStatus = fuelStatus;
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
        return "FuelStationLogItem{" +
                "id='" + id + '\'' +
                ", stationId='" + stationId + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", fuelStatus='" + fuelStatus + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", dayNumber=" + dayNumber +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }
}
