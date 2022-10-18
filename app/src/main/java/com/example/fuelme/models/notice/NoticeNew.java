package com.example.fuelme.models.notice;

public class NoticeNew {

    private String stationId;
    private String title;
    private String description;
    private String author;
    private String createAt;

    public NoticeNew() {
    }

    public NoticeNew(String stationId, String title, String description, String author, String createAt) {
        this.stationId = stationId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "NoticeNew{" +
                "stationId='" + stationId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", createAt='" + createAt + '\'' +
                '}';
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
