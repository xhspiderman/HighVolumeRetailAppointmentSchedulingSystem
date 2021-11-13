package com.haoxu.highvolumeretailstoreappointmentsystem.db.dao;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.mappers.AppointmentInventoryMapper;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.AppointmentInventory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class AppointmentInventoryDaoImpl implements AppointmentInventoryDao {
    @Resource
    private AppointmentInventoryMapper appointmentInventoryMapper;

    @Override
    public List<AppointmentInventory> queryAppointmentInventoryByStatus(int active) {
        return appointmentInventoryMapper.queryAppointmentInventoryByStatus(active);
    }

    @Override
    public void insertAppointmentInventory(AppointmentInventory appointmentInventory) {
        appointmentInventoryMapper.insert(appointmentInventory);
    }

    @Override
    public AppointmentInventory queryAppointmentInventoryById(int appointmentInventoryId) {
        return appointmentInventoryMapper.selectByPrimaryKey(appointmentInventoryId);
    }

    @Override
    public void updateAppointmentInventory(AppointmentInventory appointmentInventory) {
        appointmentInventoryMapper.updateByPrimaryKey(appointmentInventory);
    }

    @Override
    public boolean lockSpot(int appointmentInventoryId) {
        int result = appointmentInventoryMapper.lockSpot( appointmentInventoryId );
        if (result < 1) {
            log.error("Lock appointment inventory failed.");
            return false;
        }
        return true;
    }

    @Override
    public boolean deductSpot(int appointmentInventoryId) {
        int result = appointmentInventoryMapper.deductSpot(appointmentInventoryId);
        if (result < 1) {
            log.error("Deduct appointment inventory failed.");
            return false;
        }
        return true;
    }

    @Override
    public void revertSpot(int appointmentInventoryId) {
        appointmentInventoryMapper.revertSpot(appointmentInventoryId);
    }
}
