package com.selfProject.SearchOffline.domain.review.entity;

import com.selfProject.SearchOffline.domain.market.entity.MarketEntity;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewID;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reviewDetail;


    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<FileEntity> reviewImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marketID")
    private MarketEntity market;

    @Column(nullable = false)
    private float score;

    //리뷰 수정
    public void update(String reviewDetail, float score) {
        this.reviewDetail = reviewDetail;
        this.score = score;
    }
}
