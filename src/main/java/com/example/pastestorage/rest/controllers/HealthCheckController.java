package com.example.pastestorage.rest.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health_check")
@Log4j2
public class HealthCheckController {
    @GetMapping("/ping")
    public ResponseEntity<Void> ping() {
        log.info("Ping OK");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
