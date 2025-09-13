package com.example.SpringPremier.domain.boards.dto;

import com.example.SpringPremier.domain.boards.entity.Board;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class BoardDto {

    private Long id;

    @NotBlank(message = "코드는 필수입니다.")
    @Size(min = 3, max = 10, message = "코드는 3자 이상 10자 이하여야 합니다.")
    private String code;

    @NotBlank(message = "게시판명은 필수입니다.")
    @Size(min = 3, max = 20, message = "게시판명을 3자 이상 20자 이하여야 합니다.")
    private String name;

    @Size(max = 30, message = "설명은 30자까지 가능합니다.")
    private String description;

    //@NotBlank(message = "페이지 사이즈는 필수입니다.")
    //@Size(max = 2, message = "게시판사이즈는 10단위만 가능합니다.")
    //@Pattern(regexp = "^[0-9]", message = "숫자만 입력 가능합니다.")
    @Min(value = 1, message = "페이지 크기는 최소 1이어야 합니다.")
    @Max(10)
    private int pageSize;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String keyword;
    private boolean IsNew;
    private int number;
    private String param;


    public BoardDto(){ }

    public BoardDto(Long id, String code, String name, String description, int pageSize,
                    LocalDateTime createdAt, LocalDateTime updatedAt, boolean IsNew, int number, String param) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;

        this.pageSize = pageSize;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.IsNew = IsNew;
        this.number = number;
        this.param = param;
    }

    public BoardDto(List<Board> content, int displayNumber) {
    }


    //dto -> entity
    public Board toEntity(){
        return new Board(id, code, name, description, pageSize, createdAt, updatedAt);
    }

    //entity ->dto
    public static BoardDto fromEntity(Board board, int number, String param){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-mm-dd");

        return new BoardDto(
                board.getId(),
                board.getCode(),
                board.getName(),
                board.getDescription(),

                board.getPageSize(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                board.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24)),
                number,
                param
        );

    }


}