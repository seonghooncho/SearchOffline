package com.selfProject.SearchOffline.entity;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class MarketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marketID;

    @Column(nullable = false)
    private String marketName;

    @Column(nullable = false)
    private String businessHour;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String location;
    //사진은 즉시 로딩
    @OneToMany(mappedBy = "market", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<FileEntity> marketImages = new ArrayList<>();

    @OneToMany(mappedBy = "market", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProductEntity> products = new ArrayList<>();

    @OneToMany(mappedBy = "market", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewEntity> reviews = new ArrayList<>();


    /* Entity 업데이트 */
    public void update(String marketName, String businessHour, String phoneNumber, String location) {
        this.marketName = marketName;
        this.businessHour = businessHour;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }
}
