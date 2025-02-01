package com.cakeme.backend.controller;

import com.cakeme.backend.dto.post.PostRequestDTO;
import com.cakeme.backend.dto.post.PostResponseDTO;
import com.cakeme.backend.dto.response.ResponseDTO;
import com.cakeme.backend.entity.PostEntity;
import com.cakeme.backend.entity.UserEntity;
import com.cakeme.backend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/community")
@Tag(name = "PostController", description = "게시글 CRUD를 관리하는 컨트롤러")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "게시글 생성",
            description = "새로운 게시글을 생성하며 첨부 파일을 포함할 수 있습니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = PostRequestDTO.class)
                    )
            )
    )
    public ResponseEntity<ResponseDTO<PostResponseDTO>> createPost(@ModelAttribute PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        // 첨부파일 확인 및 처리
        String attachmentPath = null;
        if (postRequestDTO.getAttachment() != null && !postRequestDTO.getAttachment().isEmpty()) {
            attachmentPath = postRequestDTO.getAttachment().getOriginalFilename();
        }

        // PostEntity 생성
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postRequestDTO.getTitle());
        postEntity.setContent(postRequestDTO.getContent());
        postEntity.setCategory(postRequestDTO.getCategory());
        postEntity.setAttachment(attachmentPath); // 첨부파일이 없으면 null 저장
        postEntity.setAuthor(authenticatedUser);

        PostEntity createdPost = postService.createPost(postEntity);
        PostResponseDTO responseDTO = mapToResponseDTO(createdPost);

        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 생성 성공", responseDTO));
    }

    @PutMapping(value = "/post/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
    public ResponseEntity<ResponseDTO<PostResponseDTO>> updatePost(
            @PathVariable Long id,
            @ModelAttribute PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        PostEntity postEntity = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        postEntity.setTitle(postRequestDTO.getTitle());
        postEntity.setContent(postRequestDTO.getContent());
        postEntity.setCategory(postRequestDTO.getCategory());
        postEntity.setAuthor(authenticatedUser);

        // 첨부파일 업데이트 (없으면 기존 데이터 유지)
        if (postRequestDTO.getAttachment() != null && !postRequestDTO.getAttachment().isEmpty()) {
            postEntity.setAttachment(postRequestDTO.getAttachment().getOriginalFilename());
        }

        PostEntity updatedPost = postService.updatePost(postEntity);
        PostResponseDTO responseDTO = mapToResponseDTO(updatedPost);

        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 수정 성공", responseDTO));
    }

    @GetMapping("/post/{id}")
    @Operation(summary = "게시글 조회", description = "ID로 특정 게시글을 조회합니다.")
    public ResponseEntity<ResponseDTO<PostResponseDTO>> getPostById(@PathVariable Long id) {
        PostEntity postEntity = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        PostResponseDTO responseDTO = mapToResponseDTO(postEntity);
        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 조회 성공", responseDTO));
    }

    @GetMapping("/posts")
    @Operation(summary = "게시글 목록 조회", description = "모든 게시글의 목록을 조회합니다.")
    public ResponseEntity<ResponseDTO<List<PostResponseDTO>>> getAllPosts() {
        List<PostResponseDTO> postList = postService.getAllPosts().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 목록 조회 성공", postList));
    }

    @DeleteMapping("/post/{id}")
    @Operation(summary = "게시글 삭제", description = "ID로 특정 게시글을 삭제합니다.")
    public ResponseEntity<ResponseDTO<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 삭제 성공", null));
    }

    private PostResponseDTO mapToResponseDTO(PostEntity post) {
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setId(post.getId());
        responseDTO.setTitle(post.getTitle());
        responseDTO.setContent(post.getContent());
        responseDTO.setCategory(post.getCategory());
        responseDTO.setAttachment(post.getAttachment()); // null이면 그대로 유지

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

