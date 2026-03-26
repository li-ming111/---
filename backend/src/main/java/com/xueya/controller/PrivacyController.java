package com.xueya.controller;

import com.xueya.entity.PrivacySetting;
import com.xueya.service.PrivacySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/privacy")
public class PrivacyController {

    @Autowired
    private PrivacySettingService privacySettingService;

    // 获取用户隐私设置
    @GetMapping("/user/{userId}")
    public ResponseEntity<PrivacySetting> getPrivacySetting(@PathVariable Long userId) {
        PrivacySetting setting = privacySettingService.getByUserId(userId);
        if (setting != null) {
            return ResponseEntity.ok(setting);
        } else {
            // 如果用户没有隐私设置，返回默认设置
            PrivacySetting defaultSetting = new PrivacySetting();
            defaultSetting.setUserId(userId);
            defaultSetting.setLearningRecordVisible("private");
            defaultSetting.setCheckinDataVisible("private");
            defaultSetting.setPersonalInfoVisible("private");
            defaultSetting.setDataExportEnabled(false);
            defaultSetting.setLoginAlert(false);
            privacySettingService.save(defaultSetting);
            return ResponseEntity.ok(defaultSetting);
        }
    }

    // 更新隐私设置
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePrivacySetting(@RequestBody PrivacySetting setting) {
        boolean success = privacySettingService.updateById(setting);
        if (!success) {
            // 如果更新失败，尝试保存新设置
            success = privacySettingService.save(setting);
        }
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("success", success);
        response.put("message", success ? "隐私设置更新成功" : "隐私设置更新失败");
        return ResponseEntity.ok(response);
    }
}
