package com.cr.campusmicroblog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cr
 * @date 2020-11-17 13:18
 */
@RestController
public class SettingController {
    @GetMapping("/setting")
    public String setting() {
        return "Setting:OK";
    }
}
