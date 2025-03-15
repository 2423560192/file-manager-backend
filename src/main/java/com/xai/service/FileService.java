package com.xai.service;

import com.xai.entity.File;
import com.xai.entity.User;
import com.xai.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    
    private final FileRepository fileRepository;
    private final UserService userService;
    
    @Transactional
    public File createFile(Long userId, String filename, String content, String permission) {
        User user = userService.getUserById(userId);
        
        File file = new File();
        file.setUser(user);
        file.setFilename(filename);
        file.setContent(content != null ? content : "");
        
        // 设置权限，确保输入合法
        if (permission != null && (permission.equals("read") || permission.equals("write") || permission.equals("readwrite"))) {
            file.setPermission(permission);
        } else {
            file.setPermission("readwrite");
        }
        
        return fileRepository.save(file);
    }
    
    @Transactional(readOnly = true)
    public List<File> getFilesByUserId(Long userId) {
        // 确保用户存在
        userService.getUserById(userId);
        return fileRepository.findByUserIdOrderByUpdateTimeDesc(userId);
    }
    
    @Transactional(readOnly = true)
    public File getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("文件不存在"));
    }
    
    @Transactional
    public void deleteFile(Long fileId) {
        File file = getFileById(fileId);
        fileRepository.delete(file);
    }
    
    @Transactional
    public File updateFileContent(Long fileId, String content) {
        File file = getFileById(fileId);
        
        // 检查文件权限
        if (!file.getPermission().contains("write")) {
            throw new RuntimeException("文件没有写入权限");
        }
        
        file.setContent(content);
        return fileRepository.save(file);
    }
    
    @Transactional
    public File updateFileName(Long fileId, String filename) {
        File file = getFileById(fileId);
        file.setFilename(filename);
        return fileRepository.save(file);
    }
    
    @Transactional
    public File updateFilePermission(Long fileId, String permission) {
        File file = getFileById(fileId);
        
        // 确保输入的权限值是合法的
        if (permission == null || (!permission.equals("read") && !permission.equals("write") && !permission.equals("readwrite"))) {
            throw new RuntimeException("权限值无效，必须是read、write或readwrite");
        }
        
        file.setPermission(permission);
        return fileRepository.save(file);
    }
} 