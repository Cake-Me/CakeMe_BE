package com.cakeme.backend.controller;

import com.cakeme.backend.dto.post.FileUploadRequestDTO;
import com.cakeme.backend.dto.post.PostRequestDTO;
import com.cakeme.backend.dto.post.PostResponseDTO;
import com.cakeme.backend.dto.response.ResponseDTO;
import com.cakeme.backend.entity.PostEntity;
import com.cakeme.backend.entity.UserEntity;
import com.cakeme.backend.service.FileStorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/community")
@Tag(name = "PostController", description = "게시글 CRUD를 관리하는 컨트롤러")
public class PostController {

    private final PostService postService;
    private final FileStorageService fileStorageService;

    public PostController(PostService postService, FileStorageService fileStorageService) {
        this.postService = postService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/post")
    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    public ResponseEntity<ResponseDTO<PostResponseDTO>> createPost(@RequestBody PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postRequestDTO.getTitle());
        postEntity.setContent(postRequestDTO.getContent());
        postEntity.setCategory(postRequestDTO.getCategory());
        postEntity.setAttachment(postRequestDTO.getAttachment());
        postEntity.setAuthor(authenticatedUser);

        PostEntity createdPost = postService.createPost(postEntity);
        PostResponseDTO responseDTO = mapToResponseDTO(createdPost);

        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 생성 성공", responseDTO));
    }

    @PutMapping("/post/{id}")
    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
    public ResponseEntity<ResponseDTO<PostResponseDTO>> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        PostEntity postEntity = new PostEntity();
        postEntity.setId(id);
        postEntity.setTitle(postRequestDTO.getTitle());
        postEntity.setContent(postRequestDTO.getContent());
        postEntity.setCategory(postRequestDTO.getCategory());
        postEntity.setAttachment(postRequestDTO.getAttachment());
        postEntity.setAuthor(authenticatedUser);

        PostEntity updatedPost = postService.updatePost(postEntity);
        PostResponseDTO responseDTO = mapToResponseDTO(updatedPost);

        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 수정 성공", responseDTO));
    }

    @GetMapping
    @Operation(summary = "게시글 목록 조회", description = "모든 게시글의 목록을 조회합니다.")
    public ResponseEntity<ResponseDTO<List<PostResponseDTO>>> getAllPosts() {
        List<PostResponseDTO> postResponseDTOs = postService.getAllPosts().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 목록 조회 성공", postResponseDTOs));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "게시글 상세 조회", description = "ID를 기준으로 특정 게시글을 조회합니다.")
    public ResponseEntity<ResponseDTO<PostResponseDTO>> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(post -> {
                    PostResponseDTO responseDTO = mapToResponseDTO(post);
                    return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 조회 성공", responseDTO));
                })
                .orElse(ResponseEntity.status(404).body(new ResponseDTO<>(404, "게시글을 찾을 수 없습니다.", null)));
    }

    @DeleteMapping("/post/{id}")
    @Operation(summary = "게시글 삭제", description = "ID를 기준으로 특정 게시글을 삭제합니다.")
    public ResponseEntity<ResponseDTO<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(new ResponseDTO<>(200, "게시글 삭제 성공", null));
    }

    @PostMapping("/post/upload")
    @Operation(summary = "파일 업로드", description = "게시글에 첨부할 파일을 업로드합니다.")
    public ResponseEntity<ResponseDTO<String>> uploadFile(@ModelAttribute FileUploadRequestDTO request) {
        try {
            String filePath = fileStorageService.storeFile(request.getFile());
            return ResponseEntity.ok(new ResponseDTO<>(200, "파일 업로드 성공", filePath));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO<>(500, "파일 업로드 실패", null));
        }
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
