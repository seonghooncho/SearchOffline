package com.selfProject.SearchOffline.domain.market.dto;

import com.selfProject.SearchOffline.domain.market.entity.MarketEntity;
import com.selfProject.SearchOffline.dto.FileDTO;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class MarketDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private Long marketID;
        private String marketName;
        private String businessHour;
        private String phoneNumber;
        private String location;

        // DTO -> Entity
        public MarketEntity toEntity() {
            return MarketEntity.builder()
                    .marketID(marketID)
                    .marketName(marketName)
                    .businessHour(businessHour)
                    .phoneNumber(phoneNumber)
                    .location(location)
                    .build();
        }
    }

    @Getter
    public static class Response {

        private final Long marketID;
        private final String marketName;
        private final String businessHour;
        private final String phoneNumber;
        private final String location;
        private final List<FileDTO.Response> marketImages;


        /* Entity -> DTO */
        public Response(MarketEntity marketEntity) {
            this.marketID = marketEntity.getMarketID();
            this.marketName = marketEntity.getMarketName();
            this.businessHour = marketEntity.getBusinessHour();
            this.phoneNumber = marketEntity.getPhoneNumber();
            this.location = marketEntity.getLocation();
            this.marketImages = marketEntity.getMarketImages().stream().map(FileDTO.Response::new).collect(Collectors.toList());
        }
    }
}
