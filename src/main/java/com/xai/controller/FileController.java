package com.xai.controller;

import com.xai.entity.File;
import com.xai.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FileController {
    
    private final FileService fileService;
    
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createFile(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            String filename = (String) request.get("filename");
            String content = (String) request.get("content");
            String permission = (String) request.get("permission");
            
            if (filename == null || filename.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("文件名不能为空"));
            }
            
            File file = fileService.createFile(userId, filename, content, permission);
            return ResponseEntity.ok(createSuccessResponse(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping("/list/{userId}")
    public ResponseEntity<Map<String, Object>> getFileList(@PathVariable Long userId) {
        try {
            List<File> files = fileService.getFilesByUserId(userId);
            List<Map<String, Object>> fileList = files.stream()
                    .map(this::convertFileToMap)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("files", fileList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping("/{fileId}")
    public ResponseEntity<Map<String, Object>> getFile(@PathVariable Long fileId) {
        try {
            File file = fileService.getFileById(fileId);
            if (!file.getPermission().contains("read")) {
                return ResponseEntity.badRequest().body(createErrorResponse("文件没有读取权限"));
            }
            return ResponseEntity.ok(createSuccessResponse(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable Long fileId) {
        try {
            fileService.deleteFile(fileId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    @PutMapping("/content/{fileId}")
    public ResponseEntity<Map<String, Object>> updateFileContent(@PathVariable Long fileId, @RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            File file = fileService.updateFileContent(fileId, content);
            return ResponseEntity.ok(createSuccessResponse(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    @PutMapping("/name/{fileId}")
    public ResponseEntity<Map<String, Object>> updateFileName(@PathVariable Long fileId, @RequestBody Map<String, String> request) {
        try {
            String filename = request.get("filename");
            if (filename == null || filename.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("文件名不能为空"));
            }
            
            File file = fileService.updateFileName(fileId, filename);
            return ResponseEntity.ok(createSuccessResponse(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    @PutMapping("/permission/{fileId}")
    public ResponseEntity<Map<String, Object>> updateFilePermission(@PathVariable Long fileId, @RequestBody Map<String, String> request) {
        try {
            String permission = request.get("permission");
            File file = fileService.updateFilePermission(fileId, permission);
            return ResponseEntity.ok(createSuccessResponse(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    private Map<String, Object> createSuccessResponse(File file) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("file", convertFileToMap(file));
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
    
    private Map<String, Object> convertFileToMap(File file) {
        Map<String, Object> fileMap = new HashMap<>();
        fileMap.put("id", file.getId());
        fileMap.put("userId", file.getUser().getId());
        fileMap.put("filename", file.getFilename());
        fileMap.put("content", file.getContent());
        fileMap.put("fileType", file.getFileType());
        fileMap.put("fileSize", file.getFileSize());
        fileMap.put("permission", file.getPermission());
        fileMap.put("createTime", file.getCreateTime());
        fileMap.put("updateTime", file.getUpdateTime());
        return fileMap;
    }
} 