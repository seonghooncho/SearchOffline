package com.selfProject.SearchOffline.dto;

import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.MarketEntity;
import com.selfProject.SearchOffline.entity.ProductEntity;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private Long productID;
        private String productName;
        private Long productPrice;
        private Long productCount;
        private String productDescription;
        private MarketEntity market;
        private List<FileEntity> productImages;

        /* DTO -> Entity */
        public ProductEntity toEntity() {
            return ProductEntity.builder()
                    .productID(productID)
                    .productName(productName)
                    .productPrice(productPrice)
                    .productCount(productCount)
                    .productDescription(productDescription)
                    .market(market)
                    .productImages(productImages)
                    .build();
        }
    }

    @Getter
    public static class Response {

        private final Long productID;
        private final String productName;
        private final Long productPrice;
        private final Long productCount;
        private final String productDescription;
        private final Long marketID;
        private final List<FileDTO.Response> productImages;

        /* Entity -> DTO */
        public Response(ProductEntity productEntity) {
            this.productID = productEntity.getProductID();
            this.productName = productEntity.getProductName();
            this.productPrice = productEntity.getProductPrice();
            this.productCount = productEntity.getProductCount();
            this.productDescription = productEntity.getProductDescription();
            this.marketID = productEntity.getMarket().getMarketID();
            this.productImages = productEntity.getProductImages().stream().map(FileDTO.Response::new).collect(Collectors.toList());

        }
    }
}
