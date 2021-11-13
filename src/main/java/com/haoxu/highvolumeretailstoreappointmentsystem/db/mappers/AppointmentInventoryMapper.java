package com.haoxu.highvolumeretailstoreappointmentsystem.db.mappers;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.AppointmentInventory;

import java.util.List;

public interface AppointmentInventoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppointmentInventory record);

    int insertSelective(AppointmentInventory record);

    AppointmentInventory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppointmentInventory record);

    int updateByPrimaryKey(AppointmentInventory record);

    List<AppointmentInventory> queryAppointmentInventoryByStatus(Integer active);

    int lockSpot(Integer appointmentInventoryId);

    int deductSpot(Integer appointmentInventoryId);

    void revertSpot(Integer appointmentInventoryId);
}