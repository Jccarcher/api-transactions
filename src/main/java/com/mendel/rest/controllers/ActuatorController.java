package com.mendel.rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ActuatorController {

    private static final Logger log = LoggerFactory.getLogger(ActuatorController.class);

    @GetMapping("/health")
    public String health() {
        log.info("GET /health called");
        return "OK";
    }

}
