package com.example.SpringPremier.domain.comment.dto;

import com.example.SpringPremier.domain.comment.entity.Comment;
import com.example.SpringPremier.domain.members.entity.Member;
import com.example.SpringPremier.domain.posts.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    private Long id;
    private long post_id;
    private String member_memberId;

    @NotBlank(message = "코드는 필수입니다.")
    @Size(max = 20, message = "닉네임은 20자 이하여야 합니다.")
    private String nickname;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDto() {
    }

    public Comment toEntity(Post post, Member member){
           return new Comment(id, post, member, nickname, content, password, createdAt, updatedAt);
    }

    //entity -> dto
    public static CommentDto createCommentDto(Comment comment) {

        Member member = comment.getMember();
        String member_id;
        if (member != null) {
            member_id = member.getMemberId();
            // 이후 memberId 사용
        } else {
            member_id = "";
            // member가 null인 경우 처리
        }

        return new CommentDto(
                comment.getId(), // 댓글 엔티티의 idx
                comment.getPost().getId(), // 댓글 엔티티가 속한 부모 게시글의 id
                member_id, // 댓글 엔티티가 속한 부모 게시글의 id
                comment.getNickname(), // 댓글 엔티티의 nickname

                comment.getContent(), // 댓글 엔티티의 body
                comment.getPassword(), // 댓글 엔티티의 password
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }



}

