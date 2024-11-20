package com.cakeme.backend.service;

import com.cakeme.backend.dto.user.AddUserRequestDTO;
import com.cakeme.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequestDTO request) {
        return userRepository.save(
                com.cakeme.backend.domain.UserEntity.builder()
                        .username(request.getUsername())
                        .userId(request.getUserId())
                        .password(bCryptPasswordEncoder.encode(request.getPassword()))
                        .build()
        ).getId();
    }

    // 사용자 ID 중복 확인
    public boolean isUserIdDuplicated(String userId) {
        return userRepository.existsByUserId(userId);
    }
}
