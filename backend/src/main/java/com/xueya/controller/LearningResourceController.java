package com.xueya.controller;

import com.xueya.entity.LearningResource;
import com.xueya.service.LearningResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
public class LearningResourceController {

    @Autowired
    private LearningResourceService learningResourceService;

    // 上传学习资源
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadResource(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("tags") String tags,
            @RequestParam("uploaderId") Long uploaderId,
            @RequestParam("uploaderName") String uploaderName,
            @RequestParam("schoolId") Long schoolId) {

        try {
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

            // 创建学习资源记录
            LearningResource resource = new LearningResource();
            resource.setTitle(title);
            resource.setDescription(description);
            resource.setFileUrl("/uploads/" + fileName);
            resource.setFileName(originalFileName);
            resource.setFileType(file.getContentType());
            resource.setFileSize(file.getSize());
            resource.setCategory(category);
            resource.setTags(tags);
            resource.setUploaderId(uploaderId);
            resource.setUploaderName(uploaderName);
            resource.setSchoolId(schoolId);
            resource.setDownloadCount(0);
            resource.setViewCount(0);
            resource.setCreateTime(LocalDateTime.now().toString());
            resource.setUpdateTime(LocalDateTime.now().toString());

            learningResourceService.save(resource);

            return ResponseEntity.ok(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败: " + e.getMessage());
        }
    }

    // 获取资源列表
    @GetMapping("/list")
    public List<LearningResource> getResources() {
        return learningResourceService.list();
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
    @PreAuthorize("hasRole('ADMIN') or #uploaderId == #resource.uploaderId")
    @PutMapping("/update")
    public ResponseEntity<LearningResource> updateResource(@RequestBody LearningResource resource) {
        resource.setUpdateTime(LocalDateTime.now().toString());
        learningResourceService.updateById(resource);
        return ResponseEntity.ok(resource);
    }

    // 删除资源
    @PreAuthorize("hasRole('ADMIN') or #uploaderId == #resource.uploaderId")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable Long id) {
        learningResourceService.removeById(id);
        return ResponseEntity.ok("资源删除成功");
    }
}
