package com.fetcher.positions.entity;

public class PositionView {
    private String id;
    private String company;
    private String location;
    private String description;
    private String type;

    public PositionView(String id, String company, String location, String description, String type) {
        this.id = id;
        this.company = company;
        this.location = location;
        this.description = description;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
