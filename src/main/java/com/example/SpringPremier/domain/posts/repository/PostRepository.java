package com.example.SpringPremier.domain.posts.repository;

import com.example.SpringPremier.domain.posts.entity.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    //목록
    Page<Post> findByBoard_code(String board_code, Pageable pageable); // Iterable → ArrayList 수정

    //검색목록
    Page<Post> findByBoard_codeAndTitleContaining(String board_code, String keyword, Pageable pageable);

    //내용
    Optional<Post> findByBoard_codeAndId(String board_code, Long id); // Iterable → ArrayList 수정

    //삭제시 비밀번호 확인
    Optional<Post> findByBoard_codeAndIdAndPassword(String board_code, Long id, String password); // Iterable → ArrayList 수정

    //답변 seq 가져오기
   Optional<Post> findTop1ByBoard_codeAndRefAndStepOrderBySeqDesc(@Param("board_code") String board_code,
                                                            @Param("ref") long ref,
                                                            @Param("step") long step);

     //답변 seq 업데이트
    @Transactional
    @Modifying
    @Query(value = "Update Post p set p.seq = p.seq+1 WHERE p.board_code=:board_code and p.id=:id and p.ref = :ref  and p.seq > :seq", nativeQuery = true)
    void updateSeq(@Param("board_code") String board_code,
                   @Param("id") Long id,
                   @Param("ref") long ref,
                   @Param("seq") long seq);


    //답변 ref 업데이트
    @Transactional
    @Modifying
    @Query(value="Update Post p set p.ref = :ref WHERE p.id=:ref", nativeQuery = true)
    void updateRef(@Param("ref") long ref);

    //게시판 관리에서 삭제시 기존 데이터 존재 여부체크
    boolean existsByBoard_code(String board_code);
}