package dev.regatta.jpa_springboot.controller;

import dev.regatta.jpa_springboot.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired private AppService appService;

    @GetMapping("/")
    public String getHello() {
        return appService.getHello();
    }
}
