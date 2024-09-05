package com.selfProject.SearchOffline.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass//자식 클래스에게 매핑 정보(@ManyToOne,@Column...)를 제공
@EntityListeners(AuditingEntityListener.class)//엔티티의 라이프사이클 이벤트를 처리하는 리스너 클래스(엔티티의 생성 및 수정 시간을 추적)를 지정
@Getter
public class BaseEntity {
    @CreationTimestamp//엔티티의 특정 필드가 데이터베이스에 처음으로 저장될 때 해당 시간을 자동으로 기록
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updatedTime;
}