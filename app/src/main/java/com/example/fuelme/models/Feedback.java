package com.example.fuelme.models;

public class Feedback {

    public String id;
    public String stationId;
    public String username;
    public String subject;
    public String description;
    public String createAt;


    public Feedback() {
    }

    public Feedback(String id, String stationId, String username, String subject, String description, String createAt) {
        this.id = id;
        this.stationId = stationId;
        this.username = username;
        this.subject = subject;
        this.description = description;
        this.createAt = createAt;
    }
    public Feedback(String username, String subject,String description, String createAt){
        this.username = username;
        this.subject = subject;
        this.description = description;
        this.createAt = createAt;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
