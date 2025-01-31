package com.cakeme.backend.service;

import com.cakeme.backend.entity.PostEntity;
import com.cakeme.backend.repository.PostRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Create
    public PostEntity createPost(PostEntity post) {
        return postRepository.save(post);
    }

    // Read (모든 게시글 조회)
    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    // Read (특정 게시글 조회)
    public Optional<PostEntity> getPostById(Long id) {
        return postRepository.findById(id);
    }

    // Update
    public PostEntity updatePost(PostEntity post) {
        return postRepository.save(post);
    }

    // Delete
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
