package com.haoxu.highvolumeretailstoreappointmentsystem.service;

import com.alibaba.fastjson.JSON;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.AppointmentDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.AppointmentInventoryDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.ShopDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Appointment;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.AppointmentInventory;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Shop;
import com.haoxu.highvolumeretailstoreappointmentsystem.kafkaMQ.KafkaMQService;
import com.haoxu.highvolumeretailstoreappointmentsystem.util.RedisService;
import com.haoxu.highvolumeretailstoreappointmentsystem.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AppointmentInventoryService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AppointmentInventoryDao appointmentInventoryDao;

    @Autowired
    private KafkaMQService kafkaMQService;

    @Autowired
    ShopDao shopDao;

    @Autowired
    AppointmentDao appointmentDao;

    private static final int APP_COUNT = 1; // Default count of appointments

    // for this is run on local machine (not in distributed system), both DATACNTER_ID and MACHINE_ID are hard coded to 1.
    private static final int DATACNTER_ID = 1;
    private static final int MACHINE_ID = 1;
    private final SnowFlake snowFlake = new SnowFlake(DATACNTER_ID, MACHINE_ID);

    /**
     * Create appointment
     * @param id AppointmentInventory ID
     * @param userId User ID
     * @return Appointment detail
     * @throws Exception MQ exception
     */
    public Appointment createAppointment(int id, int userId) throws Exception {

        // 1. query & get appointmentInventory
        AppointmentInventory appointmentInventory = appointmentInventoryDao.queryAppointmentInventoryById(id);

        // 2. create & set new appointment information
        Appointment appointment = new Appointment();

        // use snowflake algorithm to generate appointment ID
        appointment.setAppointmentNo(String.valueOf(snowFlake.nextId()));
        appointment.setAppointmentInventoryId(appointmentInventory.getId());
        appointment.setUserId(userId);
        appointment.setAppointmentCount(APP_COUNT);

        // 3. send "create appointment" message to Kafka MQ
        kafkaMQService.sendAppointmentMessage(appointment, "appointment");

        return appointment;
    }

    /**
     * check if there are appointment spots for a AppointmentInventory ID
     * @param appointmentInventoryId appointmentInventoryId
     * @return true/false
     */
    public boolean appointmentSpotValidator(int appointmentInventoryId) {
        String key = "appointment inventory:" + appointmentInventoryId; // appointment inventory is for a key, it is not related to which MySQL
        return redisService.spotDeductValidator(key);
    }


    /**
     * add appointment inventory details into redis
     * @param appointmentInventoryId
     */
    public void pushAppointmentInventoryInfoToRedis(int appointmentInventoryId) {
        AppointmentInventory appointmentInventory = appointmentInventoryDao.queryAppointmentInventoryById(appointmentInventoryId);
        redisService.setValue("appointment_inventory_detail:" + appointmentInventoryId, JSON.toJSONString(appointmentInventory));

        Shop shop = shopDao.queryShopById(appointmentInventory.getStoreId());
        redisService.setValue("shop:" + appointmentInventory.getStoreId(), JSON.toJSONString(shop));
    }

    /**
     * process confirmed appointment
     * @param appointmentNo
     */
    public void confirmAppointmentProcess(String appointmentNo) throws Exception {
        Appointment appointment = appointmentDao.queryAppointment(appointmentNo);

        // check if appointment exists or not
        if (appointment == null) {
            log.error("the appointment with the No. doesn't exist：" + appointmentNo);
            return;
            // check if appointment is unconfirmed
        } else if(appointment.getStatus() != 1 ) {
            log.error("invalid appointment status：" + appointmentNo);
            return;
        }
        log.info("confirmed appointment No.：" + appointmentNo);

        // appointment has been confirmed
        appointment.setConfirmTime(new Date());
        // status 2 means appointment has been confirmed
        appointment.setStatus(2);
        appointmentDao.updateAppointment(appointment);
        // send confirmation message of the appointment
        kafkaMQService.sendAppointmentMessage(appointment, "confirmed");
    }

    // In current project, warm up is set up to run when application starts.
    public void warmUpRedis() {
        List<AppointmentInventory> appointmentInventories = appointmentInventoryDao.queryAppointmentInventoryByStatus(1);
        StringBuilder aiIdSb = new StringBuilder(); // "id1#id2#id3..."
        System.out.println("*** Start cache warming");
        for (AppointmentInventory ai : appointmentInventories) {
            // warm up for spots of an appointment inventory
            redisService.setValue("appointment inventory:" + ai.getId(), ai.getAvailableSpot());

            // warm up for details of an appointment inventory
            redisService.setValue("appointment_inventory_detail:" + ai.getId(), JSON.toJSONString(ai));

            // get appointment inventory ID list string
            aiIdSb.append(ai.getId() + "#");

            // warm up for shop information
            Shop shop = shopDao.queryShopById(ai.getStoreId());
            redisService.setValue("shop:" + ai.getStoreId(), JSON.toJSONString(shop));
        }

        // warm up for appointment inventory ID list
        redisService.setValue("appointment_inventory_ID_list", aiIdSb.toString());
    }
}
