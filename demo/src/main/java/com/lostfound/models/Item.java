package com.lostfound.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public abstract class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String category;
    private String description;
    private String location;
    private LocalDate date;
    private String reportedByEmail;

    public Item(String name, String category, String description, String location, String reportedByEmail) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.description = description;
        this.location = location;
        this.reportedByEmail = reportedByEmail;
        this.date = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReportedByEmail() {
        return reportedByEmail;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract String getType();

    public void setDate(LocalDate localDate) {
        this.date = localDate;
    }

}
