package com.haoxu.highvolumeretailstoreappointmentsystem.component;

import com.alibaba.fastjson.JSON;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.AppointmentInventoryDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.ShopDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.AppointmentInventory;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Shop;
import com.haoxu.highvolumeretailstoreappointmentsystem.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisWarmupRunner implements ApplicationRunner {

    @Autowired
    AppointmentInventoryDao appointmentInventoryDao;

    @Autowired
    ShopDao shopDao;

    @Autowired
    RedisService redisService;

    // sync up spots and details of appointment inventory, appointment inventory ID list and store information from DB to Redis
    @Override
    public void run(ApplicationArguments args) throws Exception {
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
