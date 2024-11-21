package com.cakeme.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(nullable = false, length = 10000)
    private String content; // 게시글 내용

    @Column(nullable = false)
    private String category; // 게시글 카테고리

    @Column
    private String attachment; // 첨부파일 경로 (nullable 가능)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author; // 작성자 정보

    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성일자

    private LocalDateTime updatedAt; // 수정일자

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
