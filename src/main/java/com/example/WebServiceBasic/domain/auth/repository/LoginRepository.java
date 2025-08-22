package com.example.WebServiceBasic.domain.auth.repository;

import com.example.WebServiceBasic.domain.comment.entity.Comment;
import com.example.WebServiceBasic.domain.members.entity.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends CrudRepository<Member, Long> {

    Optional<Comment> findByMemberIdAndPassword(String memberId, String password);

}
