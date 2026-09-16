package com.simulador.financiero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SimuladorFinancieroApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimuladorFinancieroApplication.class, args);
    }
}