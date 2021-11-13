package com.haoxu.highvolumeretailstoreappointmentsystem;

import com.haoxu.highvolumeretailstoreappointmentsystem.service.AppointmentInventoryService;
import com.haoxu.highvolumeretailstoreappointmentsystem.util.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class HighVolumeRetailStoreAppointmentSystemApplicationTests {

    @Autowired
    RedisService redisService;

    @Autowired
    AppointmentInventoryService appointmentInventoryService;

    @Test
    public void warmUpRedisTest() {
        appointmentInventoryService.warmUpRedis();
    }

}
