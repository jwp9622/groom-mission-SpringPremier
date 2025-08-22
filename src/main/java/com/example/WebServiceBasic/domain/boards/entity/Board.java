package com.example.WebServiceBasic.domain.boards.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)  //JPA Entity 에 이벤트가 발생할 관련 코드를 실행
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 id 자동 생성
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int pageSize;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
