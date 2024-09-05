package com.selfProject.SearchOffline.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class ReviewEntity extends BaseEntity{

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
    public void update(String reviewDetail, List<FileEntity> reviewImages, float score) {
        this.reviewDetail = reviewDetail;
        this.reviewImages = reviewImages;
        this.score = score;
    }
}
