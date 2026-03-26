package com.xueya.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueya.entity.School;
import com.xueya.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

@RestController
@RequestMapping(value = "/api/school", produces = "application/json; charset=utf-8")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    // 获取所有学校
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/list")
    public List<School> getSchoolList() {
        List<School> schools = schoolService.list();
        for (School school : schools) {
            System.out.println("School: id=" + school.getId() + ", code=" + school.getCode() + ", name=" + school.getName() + ", address=" + school.getAddress() + ", contact=" + school.getContact() + ", email=" + school.getEmail() + ", website=" + school.getWebsite());
        }
        return schools;
    }

    // 根据ID获取学校
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/{id}")
    public School getSchoolById(@PathVariable Long id) {
        return schoolService.getById(id);
    }

    // 新增学校
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/add")
    public boolean addSchool(@RequestBody School school) {
        return schoolService.save(school);
    }

    // 更新学校
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @PutMapping("/{id}")
    public boolean updateSchool(@PathVariable Long id, @RequestBody School school) {
        school.setId(id);
        return schoolService.updateById(school);
    }

    // 删除学校
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public boolean deleteSchool(@PathVariable Long id) {
        return schoolService.removeById(id);
    }

    // 根据学校代码获取学校
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/code/{code}")
    public School getSchoolByCode(@PathVariable String code) {
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return schoolService.getOne(queryWrapper);
    }

    // 测试端点，返回学校数据
    @GetMapping("/test/data")
    public List<School> testSchoolData() {
        List<School> schools = schoolService.list();
        System.out.println("测试端点获取到的学校列表：");
        for (School school : schools) {
            System.out.println("School: id=" + school.getId() + ", code=" + school.getCode() + ", name=" + school.getName() + ", address=" + school.getAddress() + ", contact=" + school.getContact() + ", email=" + school.getEmail() + ", website=" + school.getWebsite());
        }
        return schools;
    }

    // 不需要认证的测试端点，用于快速验证数据
    @GetMapping("/test/all-schools")
    public List<School> getAllSchools() {
        List<School> schools = schoolService.list();
        System.out.println("获取所有学校：");
        for (School school : schools) {
            System.out.println("School: id=" + school.getId() + ", code=" + school.getCode() + ", name=" + school.getName());
        }
        return schools;
    }

    // 修复学校数据端点，更新现有数据为正确的中文字符
    @GetMapping("/fix/data")
    public String fixSchoolData() {
        try {
            // 直接更新现有学校数据
            List<School> schools = schoolService.list();
            for (School school : schools) {
                switch (school.getCode()) {
                    case "PKU":
                        school.setName("北京大学");
                        school.setAddress("北京市海淀区颐和园路5号");
                        school.setContact("010-62751234");
                        school.setEmail("info@pku.edu.cn");
                        school.setWebsite("https://www.pku.edu.cn");
                        school.setDescription("北京大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "THU":
                        school.setName("清华大学");
                        school.setAddress("北京市海淀区清华园1号");
                        school.setContact("010-62782114");
                        school.setEmail("info@tsinghua.edu.cn");
                        school.setWebsite("https://www.tsinghua.edu.cn");
                        school.setDescription("清华大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "FUDAN":
                        school.setName("复旦大学");
                        school.setAddress("上海市杨浦区邯郸路220号");
                        school.setContact("021-65642222");
                        school.setEmail("info@fudan.edu.cn");
                        school.setWebsite("https://www.fudan.edu.cn");
                        school.setDescription("复旦大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "SJTU":
                        school.setName("上海交通大学");
                        school.setAddress("上海市闵行区东川路800号");
                        school.setContact("021-54740000");
                        school.setEmail("info@sjtu.edu.cn");
                        school.setWebsite("https://www.sjtu.edu.cn");
                        school.setDescription("上海交通大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "ZJU":
                        school.setName("浙江大学");
                        school.setAddress("浙江省杭州市西湖区余杭塘路866号");
                        school.setContact("0571-88273114");
                        school.setEmail("info@zju.edu.cn");
                        school.setWebsite("https://www.zju.edu.cn");
                        school.setDescription("浙江大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "USTC":
                        school.setName("中国科学技术大学");
                        school.setAddress("安徽省合肥市包河区金寨路96号");
                        school.setContact("0551-63602453");
                        school.setEmail("info@ustc.edu.cn");
                        school.setWebsite("https://www.ustc.edu.cn");
                        school.setDescription("中国科学技术大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "HIT":
                        school.setName("哈尔滨工业大学");
                        school.setAddress("黑龙江省哈尔滨市南岗区西大直街92号");
                        school.setContact("0451-86414060");
                        school.setEmail("info@hit.edu.cn");
                        school.setWebsite("https://www.hit.edu.cn");
                        school.setDescription("哈尔滨工业大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "XJTU":
                        school.setName("西安交通大学");
                        school.setAddress("陕西省西安市碑林区咸宁西路28号");
                        school.setContact("029-82668888");
                        school.setEmail("info@xjtu.edu.cn");
                        school.setWebsite("https://www.xjtu.edu.cn");
                        school.setDescription("西安交通大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "BIT":
                        school.setName("北京理工大学");
                        school.setAddress("北京市海淀区中关村南大街5号");
                        school.setContact("010-68913345");
                        school.setEmail("info@bit.edu.cn");
                        school.setWebsite("https://www.bit.edu.cn");
                        school.setDescription("北京理工大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "BUAA":
                        school.setName("北京航空航天大学");
                        school.setAddress("北京市海淀区学院路37号");
                        school.setContact("010-82317114");
                        school.setEmail("info@buaa.edu.cn");
                        school.setWebsite("https://www.buaa.edu.cn");
                        school.setDescription("北京航空航天大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "TONGJI":
                        school.setName("同济大学");
                        school.setAddress("上海市杨浦区四平路1239号");
                        school.setContact("021-65982200");
                        school.setEmail("info@tongji.edu.cn");
                        school.setWebsite("https://www.tongji.edu.cn");
                        school.setDescription("同济大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "WHU":
                        school.setName("武汉大学");
                        school.setAddress("湖北省武汉市武昌区八一路299号");
                        school.setContact("027-68755114");
                        school.setEmail("info@whu.edu.cn");
                        school.setWebsite("https://www.whu.edu.cn");
                        school.setDescription("武汉大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "HUST":
                        school.setName("华中科技大学");
                        school.setAddress("湖北省武汉市洪山区珞喻路1037号");
                        school.setContact("027-87541114");
                        school.setEmail("info@hust.edu.cn");
                        school.setWebsite("https://www.hust.edu.cn");
                        school.setDescription("华中科技大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "SYSU":
                        school.setName("中山大学");
                        school.setAddress("广东省广州市海珠区新港西路135号");
                        school.setContact("020-84113388");
                        school.setEmail("info@sysu.edu.cn");
                        school.setWebsite("https://www.sysu.edu.cn");
                        school.setDescription("中山大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "SCUT":
                        school.setName("华南理工大学");
                        school.setAddress("广东省广州市天河区五山路381号");
                        school.setContact("020-87110000");
                        school.setEmail("info@scut.edu.cn");
                        school.setWebsite("https://www.scut.edu.cn");
                        school.setDescription("华南理工大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "SCU":
                        school.setName("四川大学");
                        school.setAddress("四川省成都市武侯区一环路南一段24号");
                        school.setContact("028-85406666");
                        school.setEmail("info@scu.edu.cn");
                        school.setWebsite("https://www.scu.edu.cn");
                        school.setDescription("四川大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "UESTC":
                        school.setName("电子科技大学");
                        school.setAddress("四川省成都市郫都区西源大道2006号");
                        school.setContact("028-61830114");
                        school.setEmail("info@uestc.edu.cn");
                        school.setWebsite("https://www.uestc.edu.cn");
                        school.setDescription("电子科技大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "TJU":
                        school.setName("天津大学");
                        school.setAddress("天津市南开区卫津路92号");
                        school.setContact("022-27403536");
                        school.setEmail("info@tju.edu.cn");
                        school.setWebsite("https://www.tju.edu.cn");
                        school.setDescription("天津大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "NANKAI":
                        school.setName("南开大学");
                        school.setAddress("天津市南开区卫津路94号");
                        school.setContact("022-23508219");
                        school.setEmail("info@nankai.edu.cn");
                        school.setWebsite("https://www.nankai.edu.cn");
                        school.setDescription("南开大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "SDU":
                        school.setName("山东大学");
                        school.setAddress("山东省济南市历城区山大南路27号");
                        school.setContact("0531-88364737");
                        school.setEmail("info@sdu.edu.cn");
                        school.setWebsite("https://www.sdu.edu.cn");
                        school.setDescription("山东大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "JLU":
                        school.setName("吉林大学");
                        school.setAddress("吉林省长春市朝阳区前进大街2699号");
                        school.setContact("0431-85168888");
                        school.setEmail("info@jlu.edu.cn");
                        school.setWebsite("https://www.jlu.edu.cn");
                        school.setDescription("吉林大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "XMU":
                        school.setName("厦门大学");
                        school.setAddress("福建省厦门市思明区思明南路422号");
                        school.setContact("0592-2186110");
                        school.setEmail("info@xmu.edu.cn");
                        school.setWebsite("https://www.xmu.edu.cn");
                        school.setDescription("厦门大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "SEU":
                        school.setName("东南大学");
                        school.setAddress("江苏省南京市玄武区四牌楼2号");
                        school.setContact("025-83792452");
                        school.setEmail("info@seu.edu.cn");
                        school.setWebsite("https://www.seu.edu.cn");
                        school.setDescription("东南大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "DUT":
                        school.setName("大连理工大学");
                        school.setAddress("辽宁省大连市甘井子区凌工路2号");
                        school.setContact("0411-84708322");
                        school.setEmail("info@dlut.edu.cn");
                        school.setWebsite("https://www.dlut.edu.cn");
                        school.setDescription("大连理工大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "NPU":
                        school.setName("西北工业大学");
                        school.setAddress("陕西省西安市碑林区友谊西路127号");
                        school.setContact("029-88492114");
                        school.setEmail("info@nwpu.edu.cn");
                        school.setWebsite("https://www.nwpu.edu.cn");
                        school.setDescription("西北工业大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    case "CSU":
                        school.setName("中南大学");
                        school.setAddress("湖南省长沙市岳麓区麓山南路932号");
                        school.setContact("0731-88876114");
                        school.setEmail("info@csu.edu.cn");
                        school.setWebsite("https://www.csu.edu.cn");
                        school.setDescription("中南大学是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
                        break;
                    default:
                        break;
                }
                // 不更新时间字段，让数据库自动处理
                schoolService.updateById(school);
            }
            System.out.println("已更新学校数据");
            
            return "学校数据修复成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return "学校数据修复失败：" + e.getMessage();
        }
    }

    // 创建学校对象的辅助方法
    private School createSchool(String name, String code, String address, String contact, String email, String website) {
        School school = new School();
        school.setName(name);
        school.setCode(code);
        school.setAddress(address);
        school.setContact(contact);
        school.setEmail(email);
        school.setWebsite(website);
        school.setDescription(name + "是一所具有悠久历史和优良传统的综合性研究型大学，是国家'211工程'和'985工程'重点建设高校。");
        school.setCreateTime(new Date().toString());
        school.setUpdateTime(new Date().toString());
        return school;
    }
}