package com.haoxu.highvolumeretailstoreappointmentsystem.kafka;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.AppointmentDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.AppointmentInventoryDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Appointment;
import com.haoxu.highvolumeretailstoreappointmentsystem.util.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
class KafkaListeners {

    private final Logger LOG = LoggerFactory.getLogger(KafkaListeners.class);

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private AppointmentInventoryDao appointmentInventoryDao;

    @Autowired
    RedisService redisService;

    private static final int VALID = 1, INVALID = 0;

    @KafkaListener(topics = { "appointment" }, groupId = "group_1", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void onAppointmentCreationMessage (Appointment appointment) {
        // 1, parse request message of created appointment
        LOG.info("received request message of created appointment：：" + appointment.getAppointmentNo());
        appointment.setCreateTime(new Date());
        // 2, lock spot
        boolean lockSpotResult = appointmentInventoryDao.lockSpot(appointment.getAppointmentInventoryId());
        if (lockSpotResult) {
            // status of appointment: 0: no available spot, 1: appointment has been created, wait for confirmation
            appointment.setStatus(VALID);
            // user is added to limited user list
            redisService.addLimitedUser(appointment.getAppointmentInventoryId(), appointment.getUserId());
        } else {
            appointment.setStatus(INVALID);
        }
        // 3, insert appointment
        appointmentDao.insertAppointment(appointment);
    }

    @KafkaListener(topics = { "confirmed" }, groupId = "group_1", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void onAppointmentConfirmMessage(Appointment appointment) {
        // 1, parse request message of confirmed appointment
        LOG.info("received request message of confirm appointment：" + appointment.getAppointmentNo());
        // 2, deduct spot
        appointmentInventoryDao.deductSpot(appointment.getAppointmentInventoryId());
    }
}