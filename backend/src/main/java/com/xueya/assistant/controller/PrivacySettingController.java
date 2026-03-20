package com.xueya.assistant.controller;

import com.xueya.assistant.entity.PrivacySetting;
import com.xueya.assistant.service.PrivacySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/privacy")
public class PrivacySettingController {

    @Autowired
    private PrivacySettingService privacySettingService;

    @GetMapping("/user/{userId}")
    public PrivacySetting getPrivacySetting(@PathVariable Long userId) {
        return privacySettingService.getPrivacySettingByUserId(userId);
    }

    @PostMapping("/update")
    public void updatePrivacySetting(@RequestBody PrivacySetting setting) {
        privacySettingService.updatePrivacySetting(setting);
    }
}
