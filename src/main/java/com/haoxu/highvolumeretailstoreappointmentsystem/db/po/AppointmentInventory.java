package com.haoxu.highvolumeretailstoreappointmentsystem.db.po;

import java.util.Date;

public class AppointmentInventory {
    private Integer id;

    private String name;

    private Integer storeId;

    private String description;

    private Integer active;

    private Date startTime;

    private Date endTime;

    private Integer totalSpot;

    private Integer availableSpot;

    private Integer lockedSpot;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalSpot() {
        return totalSpot;
    }

    public void setTotalSpot(Integer totalSpot) {
        this.totalSpot = totalSpot;
    }

    public Integer getAvailableSpot() {
        return availableSpot;
    }

    public void setAvailableSpot(Integer availableSpot) {
        this.availableSpot = availableSpot;
    }

    public Integer getLockedSpot() {
        return lockedSpot;
    }

    public void setLockedSpot(Integer lockedSpot) {
        this.lockedSpot = lockedSpot;
    }
}