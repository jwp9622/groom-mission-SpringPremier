package com.example.SpringPremier.domain.posts.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Setter

//list api에서 페이지 나눌때 사용
public class PageResponseDto<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long  totalElements;

    private int startPage;
    private int endPage;
    private boolean hasPrev;
    private boolean hasNext;
    private int blockSize;

    public PageResponseDto(Page<T> page, int blockSize){
        this.content = page.getContent();
        this.currentPage = page.getNumber()+1;

        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();

        int tempEnd = (int) (Math.ceil((double) currentPage / blockSize) * blockSize);
        this.startPage = Math.max(tempEnd - blockSize + 1, 1);
        this.endPage = Math.min(tempEnd, totalPages);

        this.hasPrev = startPage > 1;
        this.hasNext = endPage < totalPages;
        this.blockSize = blockSize;

    }


}
