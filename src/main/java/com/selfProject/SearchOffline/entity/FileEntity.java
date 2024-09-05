package com.selfProject.SearchOffline.entity;

import lombok.*;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class FileEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileID;

    @Column
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column
    private ImageFileEnum fileCategory;

    @Column(nullable = false)
    private Long entityId;//연관 엔티티의 아이디
}
