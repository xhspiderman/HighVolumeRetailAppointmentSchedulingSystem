package com.haoxu.highvolumeretailstoreappointmentsystem.db.dao;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.AppointmentInventory;

import java.util.List;

public interface AppointmentInventoryDao {
    public List<AppointmentInventory> queryAppointmentInventoryByStatus(int activityStatus);

    public void insertAppointmentInventory(AppointmentInventory appointmentInventory);

    public AppointmentInventory queryAppointmentInventoryById(int appointmentInventoryId);

    public void updateAppointmentInventory(AppointmentInventory appointmentInventory);

    public boolean lockSpot(int appointmentInventoryId);

    public boolean deductSpot(int appointmentInventoryId);

    public void revertSpot(int appointmentInventoryId);
}
