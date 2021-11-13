package com.haoxu.highvolumeretailstoreappointmentsystem.db.po;

import java.util.Date;

public class Appointment {
    private Integer id;

    private String appointmentNo;

    private Integer status;

    private Integer appointmentInventoryId;

    private Integer userId;

    private Integer appointmentCount;

    private Date createTime;

    private Date confirmTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo == null ? null : appointmentNo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAppointmentInventoryId() {
        return appointmentInventoryId;
    }

    public void setAppointmentInventoryId(Integer appointmentInventoryId) {
        this.appointmentInventoryId = appointmentInventoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(Integer appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }
}