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
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long productPrice;

    @Column(nullable = false)
    private Long productCount;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String productDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marketID")
    private MarketEntity market;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<FileEntity> productImages = new ArrayList<>();

    /* Entity 업데이트 */
    public void update(String productName, Long productPrice, Long productCount, String productDescription, List<FileEntity> productImages) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCount = productCount;
        this.productDescription = productDescription;
        this.productImages = productImages;
    }
    public void changeCount(Long productCount){
        this.productCount = productCount;
    }
}
