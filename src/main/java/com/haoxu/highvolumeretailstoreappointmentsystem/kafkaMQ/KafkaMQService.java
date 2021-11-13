package com.haoxu.highvolumeretailstoreappointmentsystem.kafkaMQ;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMQService {

    private final Logger LOG = LoggerFactory.getLogger(KafkaMQService.class);

    @Autowired
    private KafkaTemplate<String, Appointment> appointmentKafkaTemplate;

    // Send kafka message of Appointment class.
    public void sendAppointmentMessage(Appointment appointment,String topicName) {
        LOG.info("Sending Json Serializer : {}", appointment);
        LOG.info("--------------------------------");
        appointmentKafkaTemplate.send(topicName, appointment);
    }
}
