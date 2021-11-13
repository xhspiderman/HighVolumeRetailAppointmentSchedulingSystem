package com.haoxu.highvolumeretailstoreappointmentsystem.service;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.AppointmentInventoryDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.ShopDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.AppointmentInventory;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
// Service that create static pages, so appointments go live as static pages before launch
public class AppointmentInventoryHtmlService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private AppointmentInventoryDao appointmentInventoryDao;

    @Autowired
    private ShopDao shopDao;

    // create static html page
    public void createAppointmentInventoryHtml(int appointmentInventoryId) {

        PrintWriter writer = null;
        try {
            AppointmentInventory appointmentInventory = appointmentInventoryDao.queryAppointmentInventoryById(appointmentInventoryId);
            Shop shop = shopDao.queryShopById(appointmentInventory.getStoreId());
            // get data for the page
            Map<String, Object> resultMap = new HashMap<>();

            resultMap.put("appointmentTime", appointmentInventory.getDescription());
            resultMap.put("shopId", appointmentInventory.getStoreId());
            resultMap.put("shopName", shop.getName());
            resultMap.put("shopAddress", shop.getAddress());
            resultMap.put("appointmentInventory", appointmentInventory);
            resultMap.put("shop", shop);

            // create Thymeleaf context object
            Context context = new Context();
            // put data into context object
            context.setVariables(resultMap);

            // create output file for static html
            File file = new File("src/main/resources/templates/" + "appointment_inventory_detail_" + appointmentInventoryId + ".html");
            writer = new PrintWriter(file);
            // run template engine for static page
            templateEngine.process("appointment_inventory_detail", context, writer);
        } catch (Exception e) {
            log.error("Error during making static page：" + appointmentInventoryId);
            log.error(e.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }
}
