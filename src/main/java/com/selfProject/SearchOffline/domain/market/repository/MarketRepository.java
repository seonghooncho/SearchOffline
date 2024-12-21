package com.selfProject.SearchOffline.domain.market.repository;

import com.selfProject.SearchOffline.domain.market.entity.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Long> {

}
