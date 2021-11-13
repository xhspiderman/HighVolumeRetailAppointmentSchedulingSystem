package com.haoxu.highvolumeretailstoreappointmentsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@MapperScan("com.haoxu.highvolumeretailstoreappointmentsystem.db.mappers")
@ComponentScan(basePackages = {"com.haoxu"})
@EnableKafka
public class HighVolumeRetailStoreAppointmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighVolumeRetailStoreAppointmentSystemApplication.class, args);
    }

}
