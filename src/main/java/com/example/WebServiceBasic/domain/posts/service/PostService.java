package com.example.WebServiceBasic.domain.posts.service;

import com.example.WebServiceBasic.domain.posts.dto.PostDto;
import com.example.WebServiceBasic.domain.posts.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<PostDto> pagenation(Page<Post> list, int total, int page, int size, String keyword);
    Page<PostDto> getList(String board_code, String keyword, int page, int size);
    Post getRead(String board_code, Long id);

    Post getCreateForm(String board_code, Long id, PostDto dto);
    Post create(String board_code, Long id, PostDto dto);

    Post getEditForm(String board_code, Long id);
    Post edit(String board_code, Long id, PostDto dto);

    String password(String board_code, Long id, String mode, PostDto dto);
    Post delete(String board_code, Long id, PostDto dto);
    List<Post> createBoards(List<PostDto> dtos);

}
