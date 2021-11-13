package com.haoxu.highvolumeretailstoreappointmentsystem.db.dao;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Appointment;

public interface AppointmentDao {
    void insertAppointment(Appointment appointment);

    Appointment queryAppointment(String appointmentNo);

    void updateAppointment(Appointment appointment);
}
