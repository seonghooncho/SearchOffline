package com.selfProject.SearchOffline.domain.review.repository;

import com.selfProject.SearchOffline.domain.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByMarketId(Long marketID);
}
