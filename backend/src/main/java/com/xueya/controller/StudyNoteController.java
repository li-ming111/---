package com.xueya.controller;

import com.xueya.entity.StudyNote;
import com.xueya.service.StudyNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-notes")
public class StudyNoteController {

    @Autowired
    private StudyNoteService studyNoteService;

    // 获取用户的笔记列表
    @GetMapping("/user/{userId}")
    public List<StudyNote> getNotesByUserId(@PathVariable Long userId) {
        return studyNoteService.getNotesByUserId(userId);
    }

    // 获取笔记详情
    @GetMapping("/{id}")
    public ResponseEntity<StudyNote> getNoteById(@PathVariable Long id) {
        StudyNote note = studyNoteService.getNoteById(id);
        if (note != null) {
            // 增加浏览次数
            studyNoteService.incrementViewCount(id);
            return ResponseEntity.ok(note);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 创建笔记
    @PostMapping("/create")
    public ResponseEntity<?> createNote(@RequestBody StudyNote note) {
        boolean success = studyNoteService.createNote(note);
        if (success) {
            return ResponseEntity.ok("笔记创建成功");
        } else {
            return ResponseEntity.badRequest().body("笔记创建失败");
        }
    }

    // 更新笔记
    @PutMapping("/update")
    public ResponseEntity<?> updateNote(@RequestBody StudyNote note) {
        boolean success = studyNoteService.updateNote(note);
        if (success) {
            return ResponseEntity.ok("笔记更新成功");
        } else {
            return ResponseEntity.badRequest().body("笔记更新失败");
        }
    }

    // 删除笔记
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        boolean success = studyNoteService.deleteNote(id);
        if (success) {
            return ResponseEntity.ok("笔记删除成功");
        } else {
            return ResponseEntity.badRequest().body("笔记删除失败");
        }
    }

    // 增加点赞次数
    @PutMapping("/{id}/like")
    public ResponseEntity<?> incrementLikeCount(@PathVariable Long id) {
        boolean success = studyNoteService.incrementLikeCount(id);
        if (success) {
            return ResponseEntity.ok("点赞成功");
        } else {
            return ResponseEntity.badRequest().body("点赞失败");
        }
    }
}
