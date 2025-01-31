package com.cakeme.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "scraps")
public class ScrapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 스크랩한 사용자

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post; // 스크랩된 게시글

    @Column(nullable = false, updatable = false)
    private LocalDateTime scrappedAt; // 스크랩한 시간

    @PrePersist
    public void prePersist() {
        this.scrappedAt = LocalDateTime.now();
    }

    // Setter 없이 생성자 또는 빌더 패턴을 사용하여 객체를 설정
    public ScrapEntity(UserEntity user, PostEntity post) {
        this.user = user;
        this.post = post;
    }

    protected ScrapEntity() {} // JPA 기본 생성자
}
