package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.StudyPlan;
import com.xueya.entity.StudyPlanDetail;
import com.xueya.mapper.StudyPlanMapper;
import com.xueya.service.StudyPlanService;
import com.xueya.service.StudyPlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudyPlanServiceImpl extends ServiceImpl<StudyPlanMapper, StudyPlan> implements StudyPlanService {

    @Autowired
    private StudyPlanDetailService studyPlanDetailService;

    @Override
    public List<StudyPlan> getPlansByUserId(Long userId) {
        QueryWrapper<StudyPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<StudyPlan> getPlansBySchoolId(Long schoolId) {
        QueryWrapper<StudyPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("school_id", schoolId);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public StudyPlan getPlanById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createPlan(StudyPlan studyPlan) {
        // 检查是否已存在相同标题的学习计划
        QueryWrapper<StudyPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", studyPlan.getUserId());
        queryWrapper.eq("title", studyPlan.getTitle());
        List<StudyPlan> existingPlans = baseMapper.selectList(queryWrapper);
        if (!existingPlans.isEmpty()) {
            // 已存在相同标题的学习计划，返回false
            return false;
        }
        
        studyPlan.setCreateTime(LocalDateTime.now().toString());
        studyPlan.setUpdateTime(LocalDateTime.now().toString());
        studyPlan.setStatus("active");
        studyPlan.setProgress(0.0);
        return save(studyPlan);
    }

    @Override
    public boolean updatePlan(StudyPlan studyPlan) {
        studyPlan.setUpdateTime(LocalDateTime.now().toString());
        return updateById(studyPlan);
    }

    @Override
    public boolean deletePlan(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateProgress(Long id, Double progress) {
        StudyPlan plan = baseMapper.selectById(id);
        if (plan != null) {
            plan.setProgress(progress);
            plan.setUpdateTime(LocalDateTime.now().toString());
            // 根据进度更新状态
            if (progress >= 100) {
                plan.setStatus("completed");
            } else if (progress > 0) {
                plan.setStatus("in_progress");
            }
            return updateById(plan);
        }
        return false;
    }

    @Override
    public List<StudyPlan> getPlansByStatus(String status) {
        QueryWrapper<StudyPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public StudyPlan generateAIRecommendedPlan(Long userId, String educationType, String major, Integer years) {
        // 检查是否已存在相同类型的AI推荐计划
        QueryWrapper<StudyPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("title", educationType + major + years + "年学习计划");
        List<StudyPlan> existingPlans = baseMapper.selectList(queryWrapper);
        if (!existingPlans.isEmpty()) {
            // 已存在相同类型的AI推荐计划，更新其时间信息后返回
            StudyPlan existingPlan = existingPlans.get(0);
            existingPlan.setStartDate(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            existingPlan.setEndDate(java.time.LocalDateTime.now().plusYears(years).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            existingPlan.setUpdateTime(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            // 使用update方法确保更新所有字段
            QueryWrapper<StudyPlan> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq("id", existingPlan.getId());
            update(existingPlan, updateWrapper);
            return existingPlan;
        }
        
        // 创建学习计划
        StudyPlan plan = new StudyPlan();
        plan.setUserId(userId);
        plan.setTitle(educationType + major + years + "年学习计划");
        plan.setDescription("AI智能推荐的" + educationType + major + "专业" + years + "年学习计划");
        plan.setStartDate(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        plan.setEndDate(java.time.LocalDateTime.now().plusYears(years).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        plan.setStatus("active");
        plan.setProgress(0.0);
        plan.setCreateTime(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        plan.setUpdateTime(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // 保存学习计划
        if (save(plan)) {
            // 生成学习计划详情（任务）
            generatePlanDetails(plan.getId(), educationType, major, years);
            return plan;
        }
        return null;
    }

    private void generatePlanDetails(Long planId, String educationType, String major, Integer years) {
        // 生成不同教育类型和专业的学习计划
        if ("本科".equals(educationType)) {
            generateUndergraduatePlan(planId, major, years);
        } else if ("研究生".equals(educationType)) {
            generateGraduatePlan(planId, major, years);
        }
    }

    private void generateUndergraduatePlan(Long planId, String major, Integer years) {
        // 为每个学年生成计划
        for (int year = 1; year <= years; year++) {
            // 为每个学期生成计划
            for (int semester = 1; semester <= 2; semester++) {
                String semesterName = "第" + year + "年第" + semester + "学期";
                
                // 生成核心课程
                generateCoreCourses(planId, major, year, semester, semesterName);
                
                // 生成选修课程
                generateElectiveCourses(planId, major, year, semester, semesterName);
                
                // 生成实践环节
                generatePracticalCourses(planId, major, year, semester, semesterName);
            }
        }
    }

    private void generateGraduatePlan(Long planId, String major, Integer years) {
        // 为每个学年生成计划
        for (int year = 1; year <= years; year++) {
            String yearName = "第" + year + "年";
            
            if (year == 1) {
                // 第一年：课程学习
                generateGraduateCourses(planId, major, yearName);
            } else if (year == years) {
                // 最后一年：论文和实习
                generateThesisAndInternship(planId, major, yearName);
            } else {
                // 中间年份：研究和项目
                generateResearchAndProjects(planId, major, yearName);
            }
        }
    }

    private void generateCoreCourses(Long planId, String major, int year, int semester, String semesterName) {
        // 根据专业生成核心课程
        java.util.List<String> coreCourses = getCoreCoursesForMajor(major, year, semester);
        for (String course : coreCourses) {
            createStudyPlanDetail(planId, course, "核心课程", semesterName);
        }
    }

    private void generateElectiveCourses(Long planId, String major, int year, int semester, String semesterName) {
        // 根据专业生成选修课程
        java.util.List<String> electiveCourses = getElectiveCoursesForMajor(major, year, semester);
        for (String course : electiveCourses) {
            createStudyPlanDetail(planId, course, "选修课程", semesterName);
        }
    }

    private void generatePracticalCourses(Long planId, String major, int year, int semester, String semesterName) {
        // 根据专业生成实践环节
        java.util.List<String> practicalCourses = getPracticalCoursesForMajor(major, year, semester);
        for (String course : practicalCourses) {
            createStudyPlanDetail(planId, course, "实践环节", semesterName);
        }
    }

    private void generateGraduateCourses(Long planId, String major, String yearName) {
        // 生成研究生课程
        java.util.List<String> graduateCourses = getGraduateCoursesForMajor(major);
        for (String course : graduateCourses) {
            createStudyPlanDetail(planId, course, "研究生课程", yearName);
        }
    }

    private void generateThesisAndInternship(Long planId, String major, String yearName) {
        // 生成论文和实习
        createStudyPlanDetail(planId, "毕业论文选题", "论文环节", yearName);
        createStudyPlanDetail(planId, "毕业论文撰写", "论文环节", yearName);
        createStudyPlanDetail(planId, "毕业论文答辩", "论文环节", yearName);
        createStudyPlanDetail(planId, "专业实习", "实习环节", yearName);
    }

    private void generateResearchAndProjects(Long planId, String major, String yearName) {
        // 生成研究和项目
        createStudyPlanDetail(planId, "文献综述", "研究环节", yearName);
        createStudyPlanDetail(planId, "研究项目", "研究环节", yearName);
        createStudyPlanDetail(planId, "学术论文写作", "研究环节", yearName);
    }

    private java.util.List<String> getCoreCoursesForMajor(String major, int year, int semester) {
        java.util.List<String> courses = new java.util.ArrayList<>();
        
        // 根据专业和年级返回核心课程
        if ("计算机科学与技术".equals(major)) {
            if (year == 1) {
                if (semester == 1) {
                    courses.add("高等数学");
                    courses.add("大学物理");
                    courses.add("程序设计基础");
                    courses.add("计算机导论");
                } else {
                    courses.add("高等数学");
                    courses.add("大学物理");
                    courses.add("数据结构");
                    courses.add("离散数学");
                }
            } else if (year == 2) {
                if (semester == 1) {
                    courses.add("操作系统");
                    courses.add("计算机网络");
                    courses.add("数据库原理");
                    courses.add("算法分析与设计");
                } else {
                    courses.add("编译原理");
                    courses.add("计算机组成原理");
                    courses.add("软件工程");
                    courses.add("人工智能");
                }
            } else if (year == 3) {
                if (semester == 1) {
                    courses.add("机器学习");
                    courses.add("云计算");
                    courses.add("网络安全");
                    courses.add("嵌入式系统");
                } else {
                    courses.add("大数据技术");
                    courses.add("区块链");
                    courses.add("移动应用开发");
                    courses.add("人机交互");
                }
            } else if (year == 4) {
                if (semester == 1) {
                    courses.add("毕业设计");
                    courses.add("专业实习");
                } else {
                    courses.add("毕业设计");
                    courses.add("就业指导");
                }
            }
        } else if ("金融学".equals(major)) {
            // 金融学核心课程
            if (year == 1) {
                if (semester == 1) {
                    courses.add("高等数学");
                    courses.add("微观经济学");
                    courses.add("政治经济学");
                    courses.add("会计学");
                } else {
                    courses.add("高等数学");
                    courses.add("宏观经济学");
                    courses.add("统计学");
                    courses.add("财务管理");
                }
            } else if (year == 2) {
                if (semester == 1) {
                    courses.add("货币银行学");
                    courses.add("国际金融");
                    courses.add("金融市场学");
                    courses.add("计量经济学");
                } else {
                    courses.add("商业银行经营管理");
                    courses.add("证券投资学");
                    courses.add("保险学");
                    courses.add("金融工程");
                }
            } else if (year == 3) {
                if (semester == 1) {
                    courses.add("公司金融");
                    courses.add("金融风险管理");
                    courses.add("金融监管");
                    courses.add("金融计量学");
                } else {
                    courses.add("金融衍生品");
                    courses.add("行为金融学");
                    courses.add("互联网金融");
                    courses.add("金融科技");
                }
            } else if (year == 4) {
                if (semester == 1) {
                    courses.add("毕业设计");
                    courses.add("专业实习");
                } else {
                    courses.add("毕业设计");
                    courses.add("就业指导");
                }
            }
        }
        // 可以添加更多专业的核心课程
        
        return courses;
    }

    private java.util.List<String> getElectiveCoursesForMajor(String major, int year, int semester) {
        java.util.List<String> courses = new java.util.ArrayList<>();
        
        // 根据专业和年级返回选修课程
        if ("计算机科学与技术".equals(major)) {
            if (year >= 2) {
                if (semester == 1) {
                    courses.add("Web前端开发");
                    courses.add("移动应用开发");
                } else {
                    courses.add("游戏开发");
                    courses.add("数据可视化");
                }
            }
        } else if ("金融学".equals(major)) {
            if (year >= 2) {
                if (semester == 1) {
                    courses.add("房地产金融");
                    courses.add("国际结算");
                } else {
                    courses.add("金融英语");
                    courses.add("投资银行学");
                }
            }
        }
        // 可以添加更多专业的选修课程
        
        return courses;
    }

    private java.util.List<String> getPracticalCoursesForMajor(String major, int year, int semester) {
        java.util.List<String> courses = new java.util.ArrayList<>();
        
        // 根据专业和年级返回实践课程
        if ("计算机科学与技术".equals(major)) {
            if (year == 1) {
                if (semester == 2) {
                    courses.add("程序设计实践");
                }
            } else if (year == 2) {
                if (semester == 2) {
                    courses.add("数据结构实践");
                }
            } else if (year == 3) {
                if (semester == 2) {
                    courses.add("软件工程实践");
                }
            } else if (year == 4) {
                if (semester == 1) {
                    courses.add("专业实习");
                }
            }
        } else if ("金融学".equals(major)) {
            if (year == 2) {
                if (semester == 2) {
                    courses.add("金融模拟实验");
                }
            } else if (year == 3) {
                if (semester == 2) {
                    courses.add("证券投资模拟");
                }
            } else if (year == 4) {
                if (semester == 1) {
                    courses.add("专业实习");
                }
            }
        }
        // 可以添加更多专业的实践课程
        
        return courses;
    }

    private java.util.List<String> getGraduateCoursesForMajor(String major) {
        java.util.List<String> courses = new java.util.ArrayList<>();
        
        // 根据专业返回研究生课程
        if ("计算机科学与技术".equals(major)) {
            courses.add("高级算法设计");
            courses.add("高级操作系统");
            courses.add("高级计算机网络");
            courses.add("机器学习高级课程");
            courses.add("大数据分析");
            courses.add("人工智能前沿");
        } else if ("金融学".equals(major)) {
            courses.add("高级微观经济学");
            courses.add("高级宏观经济学");
            courses.add("金融经济学");
            courses.add("高级计量经济学");
            courses.add("金融市场与机构");
            courses.add("金融风险管理");
        }
        // 可以添加更多专业的研究生课程
        
        return courses;
    }

    private void createStudyPlanDetail(Long planId, String taskName, String type, String period) {
        // 创建StudyPlanDetail对象
        StudyPlanDetail detail = new StudyPlanDetail();
        detail.setPlanId(planId);
        detail.setTaskName(taskName);
        detail.setDescription(taskName + " - " + type + " - " + period);
        detail.setStartDate(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        detail.setEndDate(java.time.LocalDateTime.now().plusMonths(3).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        detail.setStatus("active");
        detail.setProgress(0.0);
        detail.setPriority("medium");
        detail.setCreateTime(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        detail.setUpdateTime(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // 调用createDetail方法
        studyPlanDetailService.createDetail(detail);
    }
}
