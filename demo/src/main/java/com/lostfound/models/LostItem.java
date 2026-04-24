package com.lostfound.models;

import com.lostfound.models.enums.Status;

public class LostItem extends Item {

    private static final long serialVersionUID = 2L;

    private Status status = Status.OPEN;

    public LostItem(String name, String category, String description, String location, String reportedByEmail) {
        super(name, category, description, location, reportedByEmail);
    }

    @Override
    public String getType() {
        return "Lost";
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
