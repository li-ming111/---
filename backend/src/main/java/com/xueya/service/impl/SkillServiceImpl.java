package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.SkillMapper;
import com.xueya.service.SkillService;
import com.xueya.entity.Skill;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SkillServiceImpl extends ServiceImpl<SkillMapper, Skill> implements SkillService {
    @Override
    public List<Skill> getSkillsByType(String type) {
        QueryWrapper<Skill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        return list(queryWrapper);
    }

    @Override
    public List<Skill> getSkillsByCategory(String category) {
        QueryWrapper<Skill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        return list(queryWrapper);
    }

    @Override
    public Skill getSkillById(Long id) {
        return getById(id);
    }

    @Override
    public boolean createSkill(Skill skill) {
        return save(skill);
    }

    @Override
    public boolean updateSkill(Skill skill) {
        return updateById(skill);
    }

    @Override
    public boolean deleteSkill(Long id) {
        return removeById(id);
    }
}