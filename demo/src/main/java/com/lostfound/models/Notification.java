package com.lostfound.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.lostfound.utils.SessionManager;

public class Notification implements Serializable {

    private static final long serialVersionUID = 4L;

    private String id;
    private String name;
    private String location;
    private String category;
    private String reportedByEmail;
    private LocalDate date;
    private String receiverEmail;
    private String itemId;

    public Notification(String itemId, String name, String location, String category, String reportedByEmail,
            LocalDate date, String receiverEmail) {
        this.itemId = itemId;
        this.id = "NOTIF-" + UUID.randomUUID().toString();
        this.name = name;
        this.location = location;
        this.category = category;
        this.reportedByEmail = reportedByEmail;
        this.date = date;
        this.receiverEmail = receiverEmail;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReportedByEmail() {
        return reportedByEmail;
    }

    public String getId() {
        return id;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getItemId() {
        return itemId;
    }

}
