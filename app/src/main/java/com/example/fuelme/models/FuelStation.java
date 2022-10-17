package com.example.fuelme.models;

/*
* IT19014128
* A.M.W.W.R.L. Wataketiya
* Model class for fuel station
* Implements Serializable to be able to be passed as extras objects with Activity intents
* */

import androidx.annotation.NonNull;

import java.io.Serializable;

public class FuelStation implements Serializable {
    //unique id
    private String id;
    //station data
    private String license;
    private String ownerUsername;
    private String stationName;
    private String stationAddress;
    private String stationPhoneNumber;
    private String stationEmail;
    private String stationWebsite;

    //station status
    private String openStatus;
    private int petrolQueueLength;
    private int dieselQueueLength;
    private String petrolStatus;
    private String dieselStatus;

    //location data
    private double locationLatitude;
    private double locationLongitude;

    //default constructor
    public FuelStation() {

    }

    //overloaded constructor

    public FuelStation(String id, String license, String ownerUsername,
                       String stationName, String stationAddress, String stationPhoneNumber, String stationEmail, String stationWebsite,
                       String openStatus, int petrolQueueLength, int dieselQueueLength, String petrolStatus, String dieselStatus,
                       double locationLatitude, double locationLongitude) {
        this.id = id;
        this.license = license;
        this.ownerUsername = ownerUsername;
        this.stationName = stationName;
        this.stationAddress = stationAddress;
        this.stationPhoneNumber = stationPhoneNumber;
        this.stationEmail = stationEmail;
        this.stationWebsite = stationWebsite;
        this.openStatus = openStatus;
        this.petrolQueueLength = petrolQueueLength;
        this.dieselQueueLength = dieselQueueLength;
        this.petrolStatus = petrolStatus;
        this.dieselStatus = dieselStatus;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public String getStationPhoneNumber() {
        return stationPhoneNumber;
    }

    public void setStationPhoneNumber(String stationPhoneNumber) {
        this.stationPhoneNumber = stationPhoneNumber;
    }

    public String getStationEmail() {
        return stationEmail;
    }

    public void setStationEmail(String stationEmail) {
        this.stationEmail = stationEmail;
    }

    public String getStationWebsite() {
        return stationWebsite;
    }

    public void setStationWebsite(String stationWebsite) {
        this.stationWebsite = stationWebsite;
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public int getPetrolQueueLength() {
        return petrolQueueLength;
    }

    public void setPetrolQueueLength(int petrolQueueLength) {
        this.petrolQueueLength = petrolQueueLength;
    }

    public int getDieselQueueLength() {
        return dieselQueueLength;
    }

    public void setDieselQueueLength(int dieselQueueLength) {
        this.dieselQueueLength = dieselQueueLength;
    }

    public String getPetrolStatus() {
        return petrolStatus;
    }

    public void setPetrolStatus(String petrolStatus) {
        this.petrolStatus = petrolStatus;
    }

    public String getDieselStatus() {
        return dieselStatus;
    }

    public void setDieselStatus(String dieselStatus) {
        this.dieselStatus = dieselStatus;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    @NonNull
    @Override
    public String toString() {
        return "FuelStation{" +
                "id='" + id + '\'' +
                ", license='" + license + '\'' +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", stationName='" + stationName + '\'' +
                ", stationAddress='" + stationAddress + '\'' +
                ", stationPhoneNumber='" + stationPhoneNumber + '\'' +
                ", stationEmail='" + stationEmail + '\'' +
                ", stationWebsite='" + stationWebsite + '\'' +
                ", openStatus='" + openStatus + '\'' +
                ", petrolQueueLength=" + petrolQueueLength +
                ", dieselQueueLength=" + dieselQueueLength +
                ", petrolStatus='" + petrolStatus + '\'' +
                ", dieselStatus='" + dieselStatus + '\'' +
                ", locationLatitude=" + locationLatitude +
                ", locationLongitude=" + locationLongitude +
                '}';
    }
}
