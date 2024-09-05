package com.selfProject.SearchOffline.repository;

import com.selfProject.SearchOffline.entity.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Long> {

}
