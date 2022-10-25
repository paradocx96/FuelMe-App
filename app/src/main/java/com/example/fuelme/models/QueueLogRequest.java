package com.example.fuelme.models;

/*
* IT19014128
* A.M.W.W.R.L. Wataketiya
*
* Model class for queue log request
* */

public class QueueLogRequest {
    private String customerUsername;
    private String stationId;
    private String stationLicense;
    private String refuelStatus;

    public QueueLogRequest() {
    }

    public QueueLogRequest(String customerUsername, String stationId, String stationLicense, String refuelStatus) {
        this.customerUsername = customerUsername;
        this.stationId = stationId;
        this.stationLicense = stationLicense;
        this.refuelStatus = refuelStatus;
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

    public String getRefuelStatus() {
        return refuelStatus;
    }

    public void setRefuelStatus(String refuelStatus) {
        this.refuelStatus = refuelStatus;
    }

    @Override
    public String toString() {
        return "QueueLogRequest{" +
                "customerUsername='" + customerUsername + '\'' +
                ", stationId='" + stationId + '\'' +
                ", stationLicense='" + stationLicense + '\'' +
                ", refuelStatus='" + refuelStatus + '\'' +
                '}';
    }
}
