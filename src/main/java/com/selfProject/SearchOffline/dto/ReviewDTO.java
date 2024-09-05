package com.selfProject.SearchOffline.dto;

import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.MarketEntity;
import com.selfProject.SearchOffline.entity.ReviewEntity;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private Long reviewID;
        private String reviewDetail;
        private List<FileEntity> reviewImages;
        private MarketEntity market;
        private float score;

        /* DTO -> Entity */
        public ReviewEntity toEntity() {
            return ReviewEntity.builder()
                    .reviewID(reviewID)
                    .reviewDetail(reviewDetail)
                    .reviewImages(reviewImages)
                    .market(market)
                    .score(score)
                    .build();
        }
    }

    @Getter
    public static class Response {

        private final Long reviewID;
        private final String reviewDetail;
        private final List<FileDTO.Response> reviewImages;
        private final Long marketID;
        private final float score;

        /* Entity -> DTO */
        public Response(ReviewEntity reviewEntity) {
            this.reviewID = reviewEntity.getReviewID();
            this.reviewDetail = reviewEntity.getReviewDetail();
            this.reviewImages = reviewEntity.getReviewImages().stream().map(FileDTO.Response::new).collect(Collectors.toList());
            this.marketID = reviewEntity.getMarket().getMarketID();
            this.score = reviewEntity.getScore();
        }
    }
}
