package com.xai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 100)
    private String filename;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "file_type", length = 10)
    private String fileType = "txt";
    
    @Column(name = "file_size")
    private Long fileSize = 0L;
    
    @Column(length = 10, nullable = false)
    private String permission = "readwrite"; // read, write, readwrite
    
    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    @PrePersist
    @PreUpdate
    public void calculateFileSize() {
        this.fileSize = content != null ? (long) content.getBytes().length : 0L;
    }
} 