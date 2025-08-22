package com.example.WebServiceBasic.global.jwt.repository;

import com.example.WebServiceBasic.global.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember_MemberId(String memberId);
    Optional<RefreshToken> findByToken(String token);
    void deleteByMember_MemberId(String memberId);

}