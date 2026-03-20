package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.StudyNote;
import java.util.List;

public interface StudyNoteService extends IService<StudyNote> {
    List<StudyNote> getNotesByGroupId(Long groupId);
    List<StudyNote> getNotesByUserId(Long userId);
    List<StudyNote> getNotesByType(String type);
    StudyNote getNoteById(Long id);
    boolean createNote(StudyNote note);
    boolean updateNote(StudyNote note);
    boolean deleteNote(Long id);
    boolean incrementViewCount(Long id);
    boolean incrementLikeCount(Long id);
}