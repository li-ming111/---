package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.StudyNoteMapper;
import com.xueya.service.StudyNoteService;
import com.xueya.assistant.entity.StudyNote;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudyNoteServiceImpl extends ServiceImpl<StudyNoteMapper, StudyNote> implements StudyNoteService {
    @Override
    public List<StudyNote> getNotesByGroupId(Long groupId) {
        QueryWrapper<StudyNote> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<StudyNote> getNotesByUserId(Long userId) {
        QueryWrapper<StudyNote> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<StudyNote> getNotesByType(String type) {
        QueryWrapper<StudyNote> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public StudyNote getNoteById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createNote(StudyNote note) {
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