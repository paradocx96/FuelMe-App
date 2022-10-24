package com.example.fuelme.helpers;

/*
* IT19014128
* A.M.W.W.R.L. Wataketiya
*
* This application has to handle dates and times
* This helper class provides methods to perform date and time related actions
* */

import com.example.fuelme.models.time.CustomDateTime;

public class DateTimeHelper {

    //takes in a custom date time object and gives out date as a string in ISO format
    public static String getDateInISOFormat(CustomDateTime customDateTime){
        int year = customDateTime.getYear();
        int month = customDateTime.getMonth();
        int day = customDateTime.getDayNumber();

        String dateString = year + "-" + month + "-" + day;
        return  dateString;
    }

    //takes in custom date time object and gives out time as string in 24 hour format
    public static String getTimeInTwentyFourHourFormat(CustomDateTime customDateTime){
        int hour = customDateTime.getHour();
        int minute = customDateTime.getMinute();
        int second = customDateTime.getSecond();

        String timeString = hour + " : " + minute + " : " + second;
        return timeString;
    }

    //converts UTC (00:00) time to Sri Lanka time (+05:30)
    public static CustomDateTime convertUTCToSLTime(CustomDateTime customDateTime){
        int year = customDateTime.getYear();
        int month = customDateTime.getMonth();
        int day = customDateTime.getDayNumber();
        int hour = customDateTime.getHour();
        int minute = customDateTime.getMinute();
        int second = customDateTime.getSecond();

        //add 5 hours and 30 minutes
        int newMinute = minute + 30;

        if (newMinute >= 60){
            //if the minutes are greater than or equal to 60 increment hour
            hour++;
            minute = newMinute - 60; //reassign minutes after removing 60 minutes
        }
        else {
            minute = newMinute;
        }

        int newHour = hour + 5; //increase hours by five

        if (newHour >= 24){
            day++;
            hour = newHour - 24;
        }
        else {
            hour = newHour;
        }

        if (day >= 29){
            if (month == 2){
                //month is february
                if (!isYearLeap(year)){
                    //if year is not leap, go to the next month and reset day to one
                    month++;
                    day = 1;
                }
            }
            else {
                //month is not february

                if (month == 4 || month == 6 || month == 9 || month == 11){
                    //months having 30 days
                    if (day == 31){
                        //days have exceeded
                        //increment month and reset day to 1
                        month++;
                        day = 1;
                    }
                }
                else {
                    //months having 31 days
                    if (day == 32){
                        //days have exceeded
                        //increment month and reset day to 1
                        month++;
                        day = 1;
                    }
                }

            }
        }

        //if months have exceeded 12 reset month and increment year
        if (month >= 13){
            month = 1;
            year++;
        }

        //create a new CustomDateTime object and return it
        CustomDateTime customDateTimeReturn = new CustomDateTime(year,month,day,hour, minute, second);

        return customDateTimeReturn;
    }

    public static boolean isYearLeap(int year){
        int divideByFourRemainder = year%4;
        int divideByHundredRemainder = year%100;
        int divideByFourHundredRemainder = year%400;

        if (divideByFourRemainder == 0){
            if (divideByFourHundredRemainder == 0){
                return true;
            }
            else {
                if (divideByHundredRemainder == 0){
                    return false;
                }
                else {
                    return true;
                }
            }
        }
        else {
            return false;
        }
    }
}
