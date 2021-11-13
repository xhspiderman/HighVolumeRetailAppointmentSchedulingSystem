package com.haoxu.highvolumeretailstoreappointmentsystem.web;

import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.AppointmentDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.AppointmentInventoryDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.dao.ShopDao;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Appointment;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.AppointmentInventory;
import com.haoxu.highvolumeretailstoreappointmentsystem.db.po.Shop;
import com.haoxu.highvolumeretailstoreappointmentsystem.service.AppointmentInventoryService;
import com.haoxu.highvolumeretailstoreappointmentsystem.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class AppointmentInventoryController {

    @Autowired
    private AppointmentInventoryDao appointmentInventoryDao;

    @Autowired
    private ShopDao shopDao;

    @Autowired
    AppointmentInventoryService appointmentInventoryService;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    RedisService redisService;


    @RequestMapping("/addAppointmentInventory")
    public String addAppointmentInventory() {
        return "add_appointment_inventory";
    }

    // main page, list of all appointment inventories
    @RequestMapping("/appointments")
    public String appointmentInventoryList(Map<String, Object> resultMap) {
        try (Entry entry = SphU.entry("appointments")) {
            String idListStr = redisService.getValue("appointment_inventory_ID_list");
            String[] idStrList = idListStr.split("#");
            List<AppointmentInventory> appointmentInventories = new ArrayList<>();
            List<String> spots = new ArrayList<>(); // String is easy for using redis
            List<Shop> shops = new ArrayList<>();

            for (String idStr : idStrList) {
                // add appointment inventory
                String appointmentInventoryStr = redisService.getValue("appointment_inventory_detail:" + idStr);
                AppointmentInventory appointmentInventory = JSON.parseObject(appointmentInventoryStr, AppointmentInventory.class);
                appointmentInventories.add(appointmentInventory);
                // add spots
                spots.add(redisService.getValue("appointment inventory:" + idStr));
                // add shops
                String shopStr = redisService.getValue("shop:" + appointmentInventory.getStoreId());
                Shop shop = JSON.parseObject(shopStr, Shop.class);
                shops.add(shop);
            }

            resultMap.put("appointmentInventories", appointmentInventories);
            resultMap.put("spots", spots);
            resultMap.put("shops", shops);
            return "appointment_inventory";
        } catch (BlockException ex) {
            log.error("Visited main page too fast. The visit has been limited" + ex.toString());
            return "wait";
        }
    }

    @RequestMapping("/addAppointmentInventoryAction")
    public String addAppointmentInventoryAction(
            @RequestParam("name") String name,
            @RequestParam("shopId") int shopId,
            @RequestParam("description") String description,
            @RequestParam("spotNumber") int spotNumber,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            Map<String, Object> resultMap
    ) throws ParseException {
        startTime = startTime.substring(0, 10) +  startTime.substring(11);
        endTime = endTime.substring(0, 10) +  endTime.substring(11);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh:mm");
        AppointmentInventory appointmentInventory = new AppointmentInventory();
        appointmentInventory.setName(name);
        appointmentInventory.setStoreId(shopId);
        appointmentInventory.setDescription(description);
        appointmentInventory.setTotalSpot(spotNumber);
        appointmentInventory.setAvailableSpot(spotNumber);
        appointmentInventory.setLockedSpot(0);
        appointmentInventory.setActive(1);
        appointmentInventory.setStartTime(format.parse(startTime));
        appointmentInventory.setEndTime(format.parse(endTime));
        appointmentInventoryDao.insertAppointmentInventory(appointmentInventory);
        resultMap.put("appointmentInventory", appointmentInventory);
        return "add_success";
    }

    @RequestMapping("/details/{appointmentInventoryId}")
    public String detailsPage(Map<String, Object> resultMap, @PathVariable int appointmentInventoryId) {
        AppointmentInventory appointmentInventory;
        Shop shop;

        String appointmentInventoryInfo = redisService.getValue("appointment_inventory_detail:" + appointmentInventoryId);
        System.out.println("Visiting:" + appointmentInventoryInfo);
        if (StringUtils.isNotEmpty(appointmentInventoryInfo)) {
            log.info("cache data in redis:" + appointmentInventoryInfo);
            appointmentInventory = JSON.parseObject(appointmentInventoryInfo, AppointmentInventory.class);
        } else {
            appointmentInventory = appointmentInventoryDao.queryAppointmentInventoryById(appointmentInventoryId);
        }

        String shopInfo = redisService.getValue("shop:" + appointmentInventory.getStoreId());
        if (StringUtils.isNotEmpty(shopInfo)) {
            log.info("cache data in redis:" + shopInfo);
            shop = JSON.parseObject(shopInfo, Shop.class);
        } else {
            shop = shopDao.queryShopById(appointmentInventory.getStoreId());
        }


        /*
        // push to Redis
        if (appintmentInventory != null) appointmentInventoryService.pushAppointmentInventoryInfoToRedis(appointmentInventoryId);
         */

        resultMap.put("appointmentInventory", appointmentInventory);
        resultMap.put("shop", shop);
        resultMap.put("appointmentTime", appointmentInventory.getDescription());
        resultMap.put("shopId", appointmentInventory.getStoreId());
        resultMap.put("shopName", shop.getName());
        resultMap.put("shopAddress", shop.getAddress());
        return "appointment_inventory_detail";
    }

    // process schduling request
    @RequestMapping("/appointment/schedule/{userId}/{appointmentInventoryId}")
    public ModelAndView scheduleAppointment(@PathVariable int userId, @PathVariable int appointmentInventoryId) {
        boolean spotValidateResult = false;

        ModelAndView modelAndView = new ModelAndView();
        try (Entry entry = SphU.entry("scheduling")) {
            /*
             * check if user is in the list of limited user
             */
            /*
            if (redisService.isInLimitedUser(appointmentInventoryId, userId)) {
                modelAndView.addObject("resultInfo", "sorry, you are in the list of limited user");
                modelAndView.setViewName("scheduling_result");
                return modelAndView;
            }
             */
            // check if user can schedule the appointment
            spotValidateResult = appointmentInventoryService.appointmentSpotValidator(appointmentInventoryId);
            if (spotValidateResult) {
                Appointment appointment = appointmentInventoryService.createAppointment(appointmentInventoryId, userId);
                modelAndView.addObject("resultInfo","Congratulaiton!" +
                        "Your appointment is creating. Please continue and confirm the appointment. " +
                        "The appointment No. is：" + appointment.getAppointmentNo());
                modelAndView.addObject("appointmentNo",appointment.getAppointmentNo());
            } else {
                modelAndView.addObject("resultInfo","Sorry, there are no appointments available");
            }
            // if there are too many visits which server cannot process
        } catch (BlockException ex) {
            log.error("Total scheduling times exceed limit");
            modelAndView.addObject("resultInfo","Sorry, we have unexpected amount of visits. Please come back later");
            // other exceptions
        } catch (Exception e) {
            log.error("Scheduling exception " + e.toString());
            modelAndView.addObject("resultInfo","Sorry, please try again");
        }
        modelAndView.setViewName("scheduling_result");
        return modelAndView;
    }

    // query appointment
    @RequestMapping("/appointment/appointmentQuery/{appointmentNo}")
    public ModelAndView appointmentQuery(@PathVariable String appointmentNo) {
        log.info("query appintment with No.：" + appointmentNo);
        Appointment appointment = appointmentDao.queryAppointment(appointmentNo);
        ModelAndView modelAndView = new ModelAndView();

        if (appointment != null) {
            modelAndView.setViewName("appointment"); // html name
            modelAndView.addObject("appointment", appointment);
            AppointmentInventory appointmentInventory = appointmentInventoryDao.queryAppointmentInventoryById(appointment.getAppointmentInventoryId());
            Shop shop = shopDao.queryShopById(appointmentInventory.getStoreId());
            modelAndView.addObject("appointmentInventory", appointmentInventory);
            modelAndView.addObject("shop", shop);
            String appointmentTime = appointmentInventory.getDescription();
            String appointmentTimeStr = appointmentTime; //((appointmentTime < 10) ? "0" : "") + appointmentTime + ":00:00";
            modelAndView.addObject("appointmentTime", appointmentTimeStr);
        } else {
            modelAndView.setViewName("appointment_wait"); // html name
        }
        return modelAndView;
    }

    // confirm appointment
    @RequestMapping("/appointment/confirmAppointment/{appointmentNo}")
    public String confirmAppointment(@PathVariable String appointmentNo) throws Exception {
        appointmentInventoryService.confirmAppointmentProcess(appointmentNo);
        return "redirect:/appointment/appointmentQuery/" + appointmentNo;
    }

    // get system time of the server
    @ResponseBody
    @RequestMapping("/appointment/getSystemTime")
    public String getSystemTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());// new Date() is the system time of the server
        return date;
    }
}