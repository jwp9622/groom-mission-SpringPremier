package com.example.WebServiceBasic.domain.comment.repository;

import com.example.WebServiceBasic.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long > {

    List<Comment> findByPost_id(Long post_id);

    List<Comment> findByPost_idAndNickname(Long post_id, String nickname);

    Optional<Comment> findByPost_idAndId(Long post_id, Long id);
}
