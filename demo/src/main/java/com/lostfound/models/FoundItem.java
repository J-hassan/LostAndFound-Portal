package com.lostfound.models;

import com.lostfound.models.enums.Status;

public class FoundItem extends Item {

    private static final long serialVersionUID = 3L;

    private Status status = Status.OPEN;

    public FoundItem(String name, String category, String description, String location, String reportedByEmail) {
        super(name, category, description, location, reportedByEmail);
    }

    @Override
    public String getType() {
        return "Found";
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
