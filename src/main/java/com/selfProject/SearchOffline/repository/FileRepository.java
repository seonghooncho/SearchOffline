package com.selfProject.SearchOffline.repository;

import com.selfProject.SearchOffline.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    // 필요한 경우 커스텀 쿼리 메서드를 추가할 수 있습니다.
}

