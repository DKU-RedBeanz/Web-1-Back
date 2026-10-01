package com.redbeanz.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "public";
    }

    @GetMapping("/user")
    public String userEndpoint() {
        return "user";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "admin";
    }
}