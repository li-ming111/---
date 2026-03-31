package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.StudyNoteMapper;
import com.xueya.service.StudyNoteService;
import com.xueya.entity.StudyNote;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudyNoteServiceImpl extends ServiceImpl<StudyNoteMapper, StudyNote> implements StudyNoteService {
    @Override
    public List<StudyNote> getNotesByGroupId(Long groupId) {
        QueryWrapper<StudyNote> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId);
        List<StudyNote> notes = baseMapper.selectList(wrapper);
        
        // 去重处理：使用Map以id为key确保唯一性
        java.util.Map<Long, StudyNote> uniqueNotesMap = new java.util.HashMap<>();
        for (StudyNote note : notes) {
            uniqueNotesMap.put(note.getId(), note);
        }
        return new java.util.ArrayList<>(uniqueNotesMap.values());
    }

    @Override
    public List<StudyNote> getNotesByUserId(Long userId) {
        System.out.println("获取用户笔记列表，用户ID: " + userId);
        QueryWrapper<StudyNote> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<StudyNote> notes = baseMapper.selectList(wrapper);
        
        // 去重处理：使用Map以id为key确保唯一性
        java.util.Map<Long, StudyNote> uniqueNotesMap = new java.util.HashMap<>();
        for (StudyNote note : notes) {
            uniqueNotesMap.put(note.getId(), note);
        }
        List<StudyNote> uniqueNotes = new java.util.ArrayList<>(uniqueNotesMap.values());
        
        System.out.println("去重前笔记数量: " + notes.size());
        System.out.println("去重后笔记数量: " + uniqueNotes.size());
        for (StudyNote note : uniqueNotes) {
            System.out.println("笔记ID: " + note.getId() + ", 标题: " + note.getTitle());
        }
        return uniqueNotes;
    }

    @Override
    public List<StudyNote> getNotesByType(String type) {
        QueryWrapper<StudyNote> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        List<StudyNote> notes = baseMapper.selectList(wrapper);
        
        // 去重处理：使用Map以id为key确保唯一性
        java.util.Map<Long, StudyNote> uniqueNotesMap = new java.util.HashMap<>();
        for (StudyNote note : notes) {
            uniqueNotesMap.put(note.getId(), note);
        }
        return new java.util.ArrayList<>(uniqueNotesMap.values());
    }

    @Override
    public StudyNote getNoteById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createNote(StudyNote note) {
        // 检查是否已存在相同标题的学习笔记
        QueryWrapper<StudyNote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", note.getUserId());
        queryWrapper.eq("title", note.getTitle());
        List<StudyNote> existingNotes = baseMapper.selectList(queryWrapper);
        if (!existingNotes.isEmpty()) {
            // 已存在相同标题的学习笔记，返回false
            return false;
        }
        return save(note);
    }

    @Override
    public boolean updateNote(StudyNote note) {
        return updateById(note);
    }

    @Override
    public boolean deleteNote(Long id) {
        return removeById(id);
    }

    @Override
    public boolean incrementViewCount(Long id) {
        StudyNote note = getById(id);
        if (note != null) {
            note.setViewCount(note.getViewCount() + 1);
            return updateById(note);
        }
        return false;
    }

    @Override
    public boolean incrementLikeCount(Long id) {
        StudyNote note = getById(id);
        if (note != null) {
            note.setLikeCount(note.getLikeCount() + 1);
            return updateById(note);
        }
        return false;
    }
}