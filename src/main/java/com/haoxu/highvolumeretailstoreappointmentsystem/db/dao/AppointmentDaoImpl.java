package com.haoxu.highvolumeretailstoreappointmentsystem.db.dao;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.mappers.AppointmentMapper;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Appointment;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class AppointmentDaoImpl implements AppointmentDao {

    @Resource
    private AppointmentMapper appointmentMapper;

    @Override
    public void insertAppointment(Appointment appointment) {
        appointmentMapper.insert(appointment);
    }

    @Override
    public Appointment queryAppointment(String appointmentNo) {
        return appointmentMapper.selectByAppointmentNo(appointmentNo);
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        appointmentMapper.updateByPrimaryKey(appointment);
    }

}
