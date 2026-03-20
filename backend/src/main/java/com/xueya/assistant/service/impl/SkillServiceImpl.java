package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.Skill;
import com.xueya.mapper.SkillMapper;
import com.xueya.assistant.service.SkillService;
import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl extends ServiceImpl<SkillMapper, Skill> implements SkillService {
}
