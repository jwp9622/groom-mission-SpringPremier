package com.example.WebServiceBasic.domain.posts.dto;

import com.example.WebServiceBasic.domain.boards.entity.Board;
import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.posts.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDto {

    private Long id;
    private long ref;
    private long step;
    private long seq;
    private String board_code;
    private String member_memberId;

    @NotBlank(message = "제목은 필수 입력입니다.")
    @Size(min = 2, max = 50, message = "제목은 2자 이상 50자 입니다.")
    private String title;

    @NotBlank(message = "이름은 필수 입력입니다.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 까지 가능합니다.")
    private String name;

    @NotBlank(message = "내용은 필수 입력입니다.")
    private String content;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 2, max = 20, message = "비밀번호는 2자 이상 20자 영문자, 숫자이여야 합니다.")
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String mode;
    private String keyword;
    //private long pref;
    private boolean IsNew;
    private int number;
    private String param;

    //일반 목록
    public PostDto(Long id, long ref, long step, long seq,
                   String board_code, String member_memberId,
                   String title, String name, String content, String password,
                    LocalDateTime createdAt, LocalDateTime updatedAt,
                   boolean IsNew, int number, String param) {
        this.id = id;
        this.ref = ref;
        this.step = step;
        this.seq = seq;
        this.board_code = board_code;
        this.member_memberId = member_memberId;
        this.title = title;
        this.name = name;
        this.content = content;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        this.IsNew = IsNew;
        this.number = number;
        this.param = param;
    }

    //api 목록
    public PostDto(Long id, String title, String name, String content, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.content = content;
        this.createdAt = createdAt;
    }

    //dto -> entity
    public Post toEntity(Board board, Member member){
        return new Post(id, ref, step, seq, board, member, title, name, content, password, createdAt, updatedAt);
    }

    //일반 entity ->dto
    public static PostDto fromEntity(Post post, int number, String param){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-mm-dd");

        Member member = post.getMember();
        String member_id;
        if (member != null) {
            member_id = member.getMemberId();
            // 이후 memberId 사용
        } else {
            member_id = "";
            // member가 null인 경우 처리
        }

        return new PostDto(
                post.getId(),
                post.getRef(),
                post.getStep(),
                post.getSeq(),

                post.getBoard().getCode(),
                member_id,

                post.getTitle(),
                post.getName(),
                post.getContent(),
                post.getPassword(),

                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24)),
                number,
                param
        );

    }


    //api entity ->dto
    public static PostDto fromEntity(Post post){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-mm-dd");

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getName(),
                post.getContent(),
                post.getCreatedAt()
        );

    }


}
