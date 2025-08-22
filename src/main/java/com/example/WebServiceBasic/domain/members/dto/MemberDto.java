package com.example.WebServiceBasic.domain.members.dto;

import com.example.WebServiceBasic.domain.auth.role.Role;
import com.example.WebServiceBasic.domain.members.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
@Setter
public class MemberDto {
    private Long id;

    @NotBlank(message = "아이디는 필수 입력입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하 영문자, 숫자이여야 합니다.")
    @Pattern(regexp = "^[a-z0-9_-]{4,20}", message = "아이디는 4자 이상 20자 이하 영문자, 숫자이여야 합니다.")
    private String memberId;

    private String role;
    
    @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}"
            ,message = "비밀번호는 6자 이상 20자 이하여야 합니다.")
    private String password;

    private String confirm_password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "이름은 필수 입력입니다.")
    private String username;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiredAt;



    private String mode;
    private String keyword;
    //private long pref;
    private boolean IsNew;
    private int number;
    private String param;

    public MemberDto(){}

    public MemberDto(Long id, String memberId, String role, String password, String email, String username,
                     LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime expiredAt,
                     boolean IsNew, int number, String param) {
        this.id = id;
        this.memberId = memberId;
        this.role = role;
        this.password = password;
        this.email = email;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expiredAt = expiredAt;

        this.IsNew = IsNew;
        this.number = number;
        this.param = param;
    }


    //dto -> entity
    public Member toEntity(Role role){
        return new Member(id, memberId, role, password, email, username, createdAt, updatedAt, expiredAt);
    }


    //entity ->dto
    public static MemberDto fromEntity(Member member, int number, String param){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-mm-dd");

        return new MemberDto(
                member.getId(),
                member.getMemberId(),
                member.getRole().toString(),
                member.getPassword(),
                member.getEmail(),
                member.getUsername(),
                member.getCreatedAt(),
                member.getUpdatedAt(),
                member.getExpiredAt(),

                member.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24)),
                number,
                param
        );
    }

}
