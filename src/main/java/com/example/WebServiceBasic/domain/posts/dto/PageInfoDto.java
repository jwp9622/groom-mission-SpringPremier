package com.example.WebServiceBasic.domain.posts.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//목록에서 번호하고 현재여부 체크
public class PageInfoDto {
    long num;
    boolean isCurrent;

    public PageInfoDto(long num, boolean isCurrent){
        this.num = num;
        this.isCurrent = isCurrent;
    }

}
