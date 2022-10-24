package com.example.fuelme.models.time;

/*
* IT19014128
* A.M.W.W.R.L. Wataketiya
*
* This is a class for structuring date time up to the second
* */

public class CustomDateTime {

    private int year;
    private int month;
    private int dayNumber;
    private int hour;
    private int minute;
    private int second;

    public CustomDateTime() {
    }

    public CustomDateTime(int year, int month, int dayNumber, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.dayNumber = dayNumber;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
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
        return "CustomDateTime{" +
                "year=" + year +
                ", month=" + month +
                ", dayNumber=" + dayNumber +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }
}
