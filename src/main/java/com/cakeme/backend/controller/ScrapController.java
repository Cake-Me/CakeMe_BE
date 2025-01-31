package com.cakeme.backend.controller;

import com.cakeme.backend.dto.post.PostResponseDTO;
import com.cakeme.backend.dto.response.ResponseDTO;
import com.cakeme.backend.entity.PostEntity;
import com.cakeme.backend.entity.UserEntity;
import com.cakeme.backend.service.PostService;
import com.cakeme.backend.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scrap")
public class ScrapController {

    private final ScrapService scrapService;
    private final PostService postService;

    public ScrapController(ScrapService scrapService, PostService postService) {
        this.scrapService = scrapService;
        this.postService = postService;
    }

    @PostMapping("/{postId}")
    @Operation(summary = "게시글 스크랩", description = "사용자가 특정 게시글을 스크랩합니다.")
    public ResponseEntity<ResponseDTO<Void>> scrapPost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        PostEntity post = postService.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        scrapService.scrapPost(authenticatedUser, post);
        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 스크랩 성공", null));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "스크랩 취소", description = "사용자가 특정 게시글의 스크랩을 취소합니다.")
    public ResponseEntity<ResponseDTO<Void>> unScrapPost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        PostEntity post = postService.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        scrapService.unScrapPost(authenticatedUser, post);
        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 스크랩 취소 성공", null));
    }

    @GetMapping("/mypage")
    @Operation(summary = "스크랩한 게시글 조회", description = "사용자가 스크랩한 모든 게시글을 상세 내용 포함하여 조회합니다.")
    public ResponseEntity<ResponseDTO<List<PostResponseDTO>>> getScrappedPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        List<PostResponseDTO> scrappedPosts = scrapService.getScrappedPosts(authenticatedUser)
                .stream()
                .map(this::mapToResponseDTO)  // 상세 내용 포함해서 변환
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO<>(200, "스크랩한 게시글 상세 조회 성공", scrappedPosts));
    }

    private PostResponseDTO mapToResponseDTO(PostEntity post) {
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setId(post.getId());
        responseDTO.setTitle(post.getTitle());
        responseDTO.setContent(post.getContent());
        responseDTO.setCategory(post.getCategory());

        if (post.getAttachment() != null) {
            responseDTO.setAttachment("/files/" + post.getAttachment());
        }

        if (post.getAuthor() != null) {
            responseDTO.setAuthor(post.getAuthor().getUsername());
            responseDTO.setAuthorId(post.getAuthor().getUserId());
        } else {
            responseDTO.setAuthor("Unknown");
            responseDTO.setAuthorId("Unknown");
        }

        return responseDTO;
    }
}
