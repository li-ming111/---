package com.xueya.assistant.controller;

import com.xueya.assistant.entity.Interest;
import com.xueya.assistant.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class InterestController {

    @Autowired
    private InterestService interestService;

    // 获取所有兴趣
    @GetMapping("/all")
    public ResponseEntity<List<Interest>> getAllInterests() {
        List<Interest> interests = interestService.list();
        return new ResponseEntity<>(interests, HttpStatus.OK);
    }

    // 按分类获取兴趣
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Interest>> getInterestsByCategory(@PathVariable String category) {
        List<Interest> interests = interestService.getInterestsByCategory(category);
        return new ResponseEntity<>(interests, HttpStatus.OK);
    }

    // 获取热门兴趣
    @GetMapping("/popular")
    public ResponseEntity<List<Interest>> getPopularInterests() {
        List<Interest> interests = interestService.getPopularInterests();
        return new ResponseEntity<>(interests, HttpStatus.OK);
    }

    // 搜索兴趣
    @GetMapping("/search")
    public ResponseEntity<List<Interest>> searchInterests(@RequestParam String keyword) {
        List<Interest> interests = interestService.searchInterests(keyword);
        return new ResponseEntity<>(interests, HttpStatus.OK);
    }

    // 创建兴趣
    @PostMapping("/create")
    public ResponseEntity<Interest> createInterest(@RequestBody Interest interest) {
        interestService.save(interest);
        return new ResponseEntity<>(interest, HttpStatus.CREATED);
    }

    // 更新兴趣
    @PutMapping("/update")
    public ResponseEntity<Interest> updateInterest(@RequestBody Interest interest) {
        interestService.updateById(interest);
        return new ResponseEntity<>(interest, HttpStatus.OK);
    }

    // 删除兴趣
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInterest(@PathVariable Long id) {
        interestService.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
