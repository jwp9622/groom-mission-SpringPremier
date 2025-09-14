package com.example.SpringPremier.domain.members.repository;

import com.example.SpringPremier.domain.members.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String MemberId);

    //로그인시 아이디와, expires null 인 값을 체크
    Optional<Member> findByMemberIdAndExpiredAt(String MemberId, LocalDateTime expired);


    //검색목록
    Page<Member> findByUsernameContaining(String keyword, Pageable pageable);

    //사용자 정보 수정
    @Transactional
    @Modifying
    @Query(value = "UPDATE Member SET password = :password, email=:email, username=:username WHERE memberId = :memberId", nativeQuery = true)
    int updateUserNative(@Param("password") String password, @Param("email") String email,
                         @Param("username") String username, @Param("memberId") String memberId);

    //아이디 중복확인
    Boolean existsByMemberId(String userId);

    //이메일 중복확인
    Boolean existsByEmail(String email);

    //특정 아이디의 이메일은 제외하고 체크
    boolean existsByEmailAndIdNot(String email, Long id);

    //아이디, 비밀번호 일치하는 값 검색
    Optional<Member> findByMemberIdAndPassword(String memberId, String password);

    
    //시큐리티 로그인 사용
    boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);



}
