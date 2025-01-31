package com.cakeme.backend.service;

import com.cakeme.backend.entity.PostEntity;
import com.cakeme.backend.entity.ScrapEntity;
import com.cakeme.backend.entity.UserEntity;
import com.cakeme.backend.repository.ScrapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapService {

    private final ScrapRepository scrapRepository;

    public ScrapService(ScrapRepository scrapRepository) {
        this.scrapRepository = scrapRepository;
    }

    // 게시글 스크랩 추가
    public void scrapPost(UserEntity user, PostEntity post) {
        if (!scrapRepository.existsByUserAndPost(user, post)) {
            scrapRepository.save(new ScrapEntity(user, post));
        }
    }

    // 게시글 스크랩 취소
    @Transactional
    public void unScrapPost(UserEntity user, PostEntity post) {
        scrapRepository.deleteByUserAndPost(user, post);
    }

    // 사용자가 스크랩한 게시글 목록 반환
    public List<PostEntity> getScrappedPosts(UserEntity user) {
        return scrapRepository.findByUser(user)
                .stream()
                .map(ScrapEntity::getPost)
                .collect(Collectors.toList());
    }
}
