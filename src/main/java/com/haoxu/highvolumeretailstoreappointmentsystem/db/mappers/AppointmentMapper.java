package com.haoxu.highvolumeretailstoreappointmentsystem.db.mappers;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Appointment;

public interface AppointmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Appointment record);

    int insertSelective(Appointment record);

    Appointment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Appointment record);

    int updateByPrimaryKey(Appointment record);

    Appointment selectByAppointmentNo(String appointmentNo);
}