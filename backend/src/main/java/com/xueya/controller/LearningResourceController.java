package com.xueya.controller;

import com.xueya.entity.LearningResource;
import com.xueya.service.LearningResourceService;
import com.xueya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/resources", produces = "application/json; charset=utf-8")
public class LearningResourceController {

    @Autowired
    private LearningResourceService learningResourceService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // 上传学习资源
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadResource(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("tags") String tags,
            @RequestParam("uploaderId") Long uploaderId,
            @RequestParam("uploaderName") String uploaderName,
            @RequestParam("schoolId") Long schoolId,
            @RequestParam("studentStage") String studentStage, HttpServletRequest request) {

        try {
            // 验证学校管理员只能上传自己学校的资源
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                // JwtUtil jwtUtil = new JwtUtil(); // 使用注入的jwtUtil实例
                String role = jwtUtil.getRoleFromToken(token);
                Long currentUserSchoolId = jwtUtil.getSchoolIdFromToken(token);
                
                // 学校管理员只能上传自己学校的资源
                if ("school_admin".equals(role) && currentUserSchoolId != null && !currentUserSchoolId.equals(schoolId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("学校管理员只能上传自己学校的资源");
                }
            }
            
            // 创建学习资源记录
            LearningResource resource = new LearningResource();
            resource.setTitle(title);
            resource.setDescription(description);
            resource.setCategory(category);
            resource.setTags(tags);
            resource.setUploaderId(uploaderId);
            resource.setUploaderName(uploaderName);
            resource.setSchoolId(schoolId);
            resource.setStudentStage(studentStage);
            resource.setDownloadCount(0);
            resource.setViewCount(0);
            resource.setCreateTime(LocalDateTime.now().toString());
            resource.setUpdateTime(LocalDateTime.now().toString());
            resource.setStatus("published");

            // 处理文件上传
            if (file != null && !file.isEmpty()) {
                // 生成唯一文件名
                String originalFileName = file.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + fileExtension;

                // 保存文件到本地（实际生产环境应使用对象存储）
                String uploadDir = "D:/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File dest = new File(uploadDir + fileName);
                file.transferTo(dest);

                resource.setFileUrl("/uploads/" + fileName);
                resource.setFileName(originalFileName);
                resource.setFileType(file.getContentType());
                resource.setFileSize(file.getSize());
            } else {
                // 没有文件上传，设置默认值
                resource.setFileUrl("#");
                resource.setFileName("无文件");
                resource.setFileType("text/plain");
                resource.setFileSize(0L);
            }

            learningResourceService.save(resource);

            return ResponseEntity.ok(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败: " + e.getMessage());
        }
    }

    // 获取资源列表（基于学生阶段）
    @GetMapping("/list")
    public List<LearningResource> getResources(HttpServletRequest request) {
        // 从JWT令牌中获取用户信息
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                // JwtUtil jwtUtil = new JwtUtil(); // 使用注入的jwtUtil实例
                Long userId = jwtUtil.getUserIdFromToken(token);
                
                // 这里需要获取用户的学生阶段信息
                // 假设我们有一个方法可以根据userId获取用户信息
                // 这里简化处理，实际应该从数据库中获取
                return learningResourceService.getResourcesByStudentStage(userId);
            } catch (Exception e) {
                // JWT令牌解析失败，返回空列表
                return new ArrayList<>();
            }
        }
        // 如果没有认证，返回空列表
        return new ArrayList<>();
    }

    // 根据学校获取资源
    @GetMapping("/school/{schoolId}")
    public List<LearningResource> getResourcesBySchool(@PathVariable Long schoolId) {
        return learningResourceService.getResourcesBySchoolId(schoolId);
    }

    // 根据分类获取资源
    @GetMapping("/category/{category}")
    public List<LearningResource> getResourcesByCategory(@PathVariable String category) {
        return learningResourceService.getResourcesByCategory(category);
    }

    // 搜索资源
    @GetMapping("/search")
    public List<LearningResource> searchResources(@RequestParam String keyword) {
        return learningResourceService.searchResources(keyword);
    }

    // 获取推荐资源
    @GetMapping("/recommended/{userId}")
    public List<LearningResource> getRecommendedResources(@PathVariable Long userId) {
        return learningResourceService.getRecommendedResources(userId);
    }

    // 获取资源详情
    @GetMapping("/{id}")
    public ResponseEntity<LearningResource> getResourceById(@PathVariable Long id) {
        LearningResource resource = learningResourceService.getById(id);
        if (resource != null) {
            // 增加浏览次数
            learningResourceService.incrementViewCount(id);
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 下载资源
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadResource(@PathVariable Long id) {
        LearningResource resource = learningResourceService.getById(id);
        if (resource != null) {
            // 增加下载次数
            learningResourceService.incrementDownloadCount(id);
            // 实际生产环境应返回文件流，这里仅返回文件信息
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 更新资源
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateResource(@RequestBody LearningResource resource, HttpServletRequest request) {
        try {
            // 验证学校管理员只能更新自己学校的资源
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                // JwtUtil jwtUtil = new JwtUtil(); // 使用注入的jwtUtil实例
                String role = jwtUtil.getRoleFromToken(token);
                Long currentUserSchoolId = jwtUtil.getSchoolIdFromToken(token);
                
                // 学校管理员只能更新自己学校的资源
                if ("school_admin".equals(role) && currentUserSchoolId != null) {
                    LearningResource existingResource = learningResourceService.getById(resource.getId());
                    if (existingResource != null && !existingResource.getSchoolId().equals(currentUserSchoolId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("学校管理员只能更新自己学校的资源");
                    }
                }
            }
            
            resource.setUpdateTime(LocalDateTime.now().toString());
            learningResourceService.updateById(resource);
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新资源失败: " + e.getMessage());
        }
    }

    // 删除资源
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable Long id, HttpServletRequest request) {
        try {
            // 验证学校管理员只能删除自己学校的资源
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                // JwtUtil jwtUtil = new JwtUtil(); // 使用注入的jwtUtil实例
                String role = jwtUtil.getRoleFromToken(token);
                Long currentUserSchoolId = jwtUtil.getSchoolIdFromToken(token);
                
                // 学校管理员只能删除自己学校的资源
                if ("school_admin".equals(role) && currentUserSchoolId != null) {
                    LearningResource existingResource = learningResourceService.getById(id);
                    if (existingResource != null && !existingResource.getSchoolId().equals(currentUserSchoolId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("学校管理员只能删除自己学校的资源");
                    }
                }
            }
            
            learningResourceService.removeById(id);
            return ResponseEntity.ok("资源删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除资源失败: " + e.getMessage());
        }
    }
}
