package com.selfProject.SearchOffline.repository;

import com.selfProject.SearchOffline.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByMarketId(Long marketId);
    @Query("SELECT p FROM ProductEntity p WHERE p.productName LIKE %:keyword%")
    List<ProductEntity> findByProductNameContaining(@Param("keyword") String keyword);
}
