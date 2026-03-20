package com.xueya.assistant.controller;

import com.xueya.assistant.entity.OfflineData;
import com.xueya.assistant.service.OfflineDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/offline-learning")
public class OfflineLearningController {

    @Autowired
    private OfflineDataService offlineDataService;

    // 保存离线学习数据
    @PostMapping("/save")
    public ResponseEntity<OfflineData> saveOfflineData(@RequestBody OfflineData offlineData) {
        offlineData.setSyncStatus("未同步");
        offlineData.setCreateTime(java.time.LocalDateTime.now());
        offlineDataService.save(offlineData);
        return new ResponseEntity<>(offlineData, HttpStatus.CREATED);
    }

    // 获取用户离线学习数据
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OfflineData>> getOfflineDataByUserId(@PathVariable Long userId) {
        List<OfflineData> offlineDataList = offlineDataService.getOfflineDataByUserId(userId);
        return new ResponseEntity<>(offlineDataList, HttpStatus.OK);
    }

    // 同步离线数据到服务器
    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncOfflineData(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        List<Long> dataIds = (List<Long>) request.get("dataIds");

        int syncedCount = 0;
        for (Long dataId : dataIds) {
            OfflineData offlineData = offlineDataService.getById(dataId);
            if (offlineData != null) {
                offlineData.setSyncStatus("已同步");
                offlineData.setSyncTime(java.time.LocalDateTime.now());
                offlineDataService.updateById(offlineData);
                syncedCount++;
            }
        }

        Map<String, Object> response = Map.of(
                "success", true,
                "syncedCount", syncedCount,
                "totalCount", dataIds.size()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 删除离线学习数据
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOfflineData(@PathVariable Long id) {
        boolean deleted = offlineDataService.removeById(id);
        Map<String, Object> response = Map.of(
                "success", deleted
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 获取未同步的离线数据
    @GetMapping("/unsynced/{userId}")
    public ResponseEntity<List<OfflineData>> getUnsyncedOfflineData(@PathVariable Long userId) {
        List<OfflineData> offlineDataList = offlineDataService.getUnsyncedOfflineData(userId);
        return new ResponseEntity<>(offlineDataList, HttpStatus.OK);
    }
}
