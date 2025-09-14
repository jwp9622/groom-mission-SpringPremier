package com.example.SpringPremier.domain.comment.entity;

import com.example.SpringPremier.domain.comment.dto.CommentDto;
import com.example.SpringPremier.domain.members.entity.Member;
import com.example.SpringPremier.domain.posts.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 이 엔티티(Comment)와 부모 엔티티(Article)를 다대일 관계로 설정
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false) // 외래키 생성, board 엔티티의 기본키(bid)와 매핑
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_memberId", referencedColumnName = "memberId", nullable = true)
    private Member member;

    @Column
    private String nickname;

    @Column
    private String content;

    @Column
    private String password;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdAt;


    @Column(nullable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public static Comment createComment(CommentDto dto, Post post, Member member) {
        // 예외 발생
        //if (dto.getId() != null)
        //    throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");
        if (dto.getPost_id() != post.getId())
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못됐습니다.");

        // 엔티티 생성 및 반환
        return new Comment(
                dto.getId(), // 댓글 아이디
                post, // 부모 게시글
                member, //회원 아이디
                dto.getNickname(), // 댓글 닉네임
                dto.getContent(), // 댓글 본문
                dto.getPassword(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    public void patch(CommentDto dto) {
        // 예외 발생
        if (this.id != dto.getId())
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력됐습니다.");

        // 객체 갱신
        if (dto.getNickname() != null) // 수정할 닉네임 데이터가 있다면
            this.nickname = dto.getNickname(); // 내용을 반영

        if (dto.getContent() != null) // 수정할 본문 데이터가 있다면
            this.content = dto.getContent(); // 내용을 반영
    }


}
