package com.xueya.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.Major;
import com.xueya.mapper.MajorMapper;
import com.xueya.service.MajorService;
import org.springframework.stereotype.Service;

@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {
}