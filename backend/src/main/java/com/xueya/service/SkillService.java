package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.Skill;
import java.util.List;

public interface SkillService extends IService<Skill> {
    List<Skill> getSkillsByType(String type);
    List<Skill> getSkillsByCategory(String category);
    Skill getSkillById(Long id);
    boolean createSkill(Skill skill);
    boolean updateSkill(Skill skill);
    boolean deleteSkill(Long id);
}