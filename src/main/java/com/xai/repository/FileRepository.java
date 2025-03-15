package com.xai.repository;

import com.xai.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    
    List<File> findByUserId(Long userId);
    
    List<File> findByUserIdOrderByUpdateTimeDesc(Long userId);
} 