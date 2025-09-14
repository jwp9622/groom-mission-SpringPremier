package com.example.SpringPremier.domain.posts.entity;

import com.example.SpringPremier.domain.boards.entity.Board;
import com.example.SpringPremier.domain.members.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@ToString
@EntityListeners(AuditingEntityListener.class)  //JPA Entity 에 이벤트가 발생할 관련 코드를 실행
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("'0'")
    private long ref;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("'0'")
    private long step;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("'0'")
    private long seq;

    @ManyToOne
    @JoinColumn(name = "board_code", referencedColumnName = "code", nullable = false)
    private Board board;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_memberId", referencedColumnName = "memberId", nullable = true)
    private Member member;

    @Column
    private String title;

    @Column
    private String name;

    @Column
    private String content;

    @Column(updatable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;



}